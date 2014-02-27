package com.codenvy.ide.factory.server;

import com.codenvy.api.factory.dto.AdvancedFactoryUrl;
import com.codenvy.api.vfs.server.MountPoint;
import com.codenvy.api.vfs.server.VirtualFile;
import com.codenvy.api.vfs.server.VirtualFileSystem;
import com.codenvy.api.vfs.server.VirtualFileSystemRegistry;
import com.codenvy.api.vfs.server.exceptions.ItemAlreadyExistException;
import com.codenvy.api.vfs.server.exceptions.ItemNotFoundException;
import com.codenvy.api.vfs.server.exceptions.VirtualFileSystemException;
import com.codenvy.api.vfs.shared.ItemType;
import com.codenvy.api.vfs.shared.PropertyFilter;
import com.codenvy.api.vfs.shared.dto.*;
import com.codenvy.commons.env.EnvironmentContext;
import com.codenvy.commons.lang.NameGenerator;
import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.git.server.GitConnectionFactory;
import com.codenvy.ide.ext.git.server.GitException;
import com.codenvy.ide.ext.git.server.rest.GitService;
import com.codenvy.ide.ext.git.shared.*;
import com.codenvy.ide.factory.server.rest.FactoryException;
import com.codenvy.ide.factory.server.variable.VariableReplacer;
import com.codenvy.ide.factory.shared.Param;
import com.codenvy.ide.rest.HTTPStatus;
import com.codenvy.organization.client.WorkspaceManager;
import com.codenvy.organization.exception.OrganizationServiceException;
import com.codenvy.organization.model.Workspace;
import com.codenvy.vfs.impl.fs.LocalPathResolver;
import com.google.inject.Inject;

import org.codenvy.mail.MailSenderClient;
import org.everrest.websockets.WSConnectionContext;
import org.everrest.websockets.message.ChannelBroadcastMessage;
import org.everrest.websockets.message.MessageConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author vzhukovskii@codenvy.com
 */
public class Factory {

    @Inject
    private MailSenderClient mailClient;

    @Inject
    private VirtualFileSystemRegistry vfsRegistry;

    @Inject
    private LocalPathResolver pathResolver;

    @Inject
    private GitService gitService;

    @Inject
    private WorkspaceManager workspaceManager;

    @Inject
    private GitConnectionFactory gitConnectionFactory;

    private static final String MAIL_FROM      = "Codenvy <noreply@codenvy.com>";
    private static final String MAIL_SUBJECT   = "Check out my Codenvy project";
    private static final String MAIL_MIME_TYPE = "text/plain; charset=utf-8";

    private static final String TEMP_BRANCH_NAME          = "temp";
    public static final  String WS_FACTORY_EVENTS_CHANNEL = "factory-events";

    private final static Logger LOG = LoggerFactory.getLogger(Factory.class);

    public void acceptFactory(AdvancedFactoryUrl factory) throws FactoryException {
        Item workingDir;

        try {
            setDefaultProjectNameIfNeed(factory);

            workingDir = createEmptyDirectory(factory);

            //update factory object in according to project name
            factory.getProjectattributes().put(Param.PROFILE_ATTRIBUTES_PNAME.toString(), workingDir.getName());

            CloneRequest cloneRequest = DtoFactory.getInstance().createDto(CloneRequest.class);
            cloneRequest.setRemoteUri(URLDecoder.decode(factory.getVcsurl(), "UTF-8"));
            cloneRequest.setWorkingDir(workingDir.getId());

            gitConnectionFactory.getConnection(getAbsolutePathToProject(workingDir)).clone(cloneRequest);

//            gitService.clone(cloneRequest);
        } catch (URISyntaxException | VirtualFileSystemException | GitException | UnsupportedEncodingException e) {
            LOG.warn("Failed to accept factory for git url: " + factory.getVcsurl(), e);
            throw new FactoryException(HTTPStatus.INTERNAL_ERROR, "Failed to accept factory", e);
        }

        if (factory.getCommitid() != null && !factory.getCommitid().trim().isEmpty()) {
            checkoutCommitId(factory.getCommitid());
        } else if (factory.getVcsbranch() != null && !factory.getVcsbranch().trim().isEmpty()) {
            checkoutBranch(factory.getVcsbranch());
        }

        replaceVariables(factory, workingDir);
    }

    private void checkoutCommitId(String commitId) {
        BranchCheckoutRequest checkoutRequest = DtoFactory.getInstance().createDto(BranchCheckoutRequest.class);
        checkoutRequest.setName(TEMP_BRANCH_NAME);
        checkoutRequest.setCreateNew(true);
        checkoutRequest.setStartPoint(commitId);
        try {
            gitService.branchCheckout(checkoutRequest);
            pushMessageToClient(String.format("Switching to commit <b>#%s</b>, temporary branch created.", commitId));
            LOG.info("Checkout to commit {}", commitId);
        } catch (GitException | VirtualFileSystemException e) {
            pushMessageToClient(String.format("Switching to commit <b>#%s</b> failed.", commitId));
            LOG.warn("Failed to checkout to commit id {}", commitId);
        }
    }

    private void checkoutBranch(String branchToCheckout) {
        BranchListRequest branchListRequest = DtoFactory.getInstance().createDto(BranchListRequest.class);
        branchListRequest.setListMode(BranchListRequest.LIST_ALL);

        try {
            for (Branch branch : gitService.branchList(branchListRequest).getEntity()) {
                String branchName = branch.getDisplayName();
                if (branchName.contains("origin")) {
                    String[] temp = branch.getDisplayName().split("/");
                    branchName = temp[temp.length - 1];
                }

                if (branchName.equals(branchToCheckout)) {
                    BranchCheckoutRequest checkoutRequest = DtoFactory.getInstance().createDto(BranchCheckoutRequest.class);
                    checkoutRequest.setName(branch.getDisplayName());
                    checkoutRequest.setStartPoint(branch.getName());
                    checkoutRequest.setCreateNew(branch.isRemote());
                    gitService.branchCheckout(checkoutRequest);
                    pushMessageToClient(String.format("Switching to branch <b>%s</b>.", branch));
                    LOG.info("Switching to branch {}", branchToCheckout);
                    return;
                }
            }
            pushMessageToClient(String.format("Switching to branch <b>%s</b> failed.", branchToCheckout));
            LOG.info("Switching to branch {} failed. Branch {} doesn't exists.", branchToCheckout, branchToCheckout);
        } catch (VirtualFileSystemException | GitException e) {
            pushMessageToClient(String.format("Switching to branch <b>%s</b> failed.", branchToCheckout));
            LOG.warn("Switching to branch {} failed.", branchToCheckout);
        }
    }

    private void replaceVariables(AdvancedFactoryUrl factory, Item workingDir) {
        if (factory.getVariables() == null || factory.getVariables().isEmpty() || workingDir == null) {
            return;
        }

        try {
            new VariableReplacer(Paths.get(getAbsolutePathToProject(workingDir))).performReplacement(factory.getVariables());
        } catch (VirtualFileSystemException e) {
            LOG.warn("Failed to get vfs provider for {}", EnvironmentContext.getCurrent().getWorkspaceId());
        }
    }

    private String getAbsolutePathToProject(Item workingDir) throws VirtualFileSystemException {
            VirtualFileSystem vfs = vfsRegistry.getProvider(EnvironmentContext.getCurrent().getWorkspaceId()).newInstance(null, null);
            if (vfs == null) {
                throw new VirtualFileSystemException(
                        "Can't resolve path on the Local File System : Virtual file system not initialized");
            }

            Item gitProject = vfs.getItemByPath(workingDir.getPath(), null, false);
            final MountPoint mountPoint = vfs.getMountPoint();
            final VirtualFile virtualFile = mountPoint.getVirtualFileById(gitProject.getId());

            return pathResolver.resolve(virtualFile);
    }

    private Item createEmptyDirectory(AdvancedFactoryUrl factory) throws VirtualFileSystemException {
        final String projectName = factory.getProjectattributes().get(Param.PROFILE_ATTRIBUTES_PNAME.toString());

        VirtualFileSystem vfs = vfsRegistry.getProvider(EnvironmentContext.getCurrent().getWorkspaceId()).newInstance(null, null);
        if (vfs == null) {
            throw new VirtualFileSystemException(
                    "Can't resolve path on the Local File System : Virtual file system not initialized");
        }

        try {
            vfs.getItemByPath(File.separator + projectName, null, false);
        } catch (ItemNotFoundException e) {
            return vfs.createFolder(vfs.getInfo().getRoot().getId(), projectName);
        } catch (ItemAlreadyExistException e) {
            return vfs.createFolder(vfs.getInfo().getRoot().getId(), NameGenerator.generate(projectName, 5));
        }

        return vfs.createFolder(vfs.getInfo().getRoot().getId(), NameGenerator.generate(projectName, 5));
    }

    private void pushMessageToClient(String message) {
        ChannelBroadcastMessage broadcastMessage = new ChannelBroadcastMessage();
        broadcastMessage.setChannel(WS_FACTORY_EVENTS_CHANNEL);
        broadcastMessage.setType(ChannelBroadcastMessage.Type.NONE);
        broadcastMessage.setBody(message);

        try {
            WSConnectionContext.sendMessage(broadcastMessage);
        } catch (MessageConversionException | IOException e) {
            LOG.warn("Failed to send message through Websockets", e);
        }
    }

    public void sendFactory(String email, String message) throws FactoryException {
        try {
            mailClient.sendMail(MAIL_FROM, email, "", MAIL_SUBJECT, MAIL_MIME_TYPE, message);
            LOG.info("Email with factory sent to {}", email);
        } catch (MessagingException | IOException e) {
            LOG.warn("Failed to send email to {}", email);
            throw new FactoryException(500, "Failed to send email to " + email, e);
        }
    }

    public void setPrivateWorkspaceAttr(AdvancedFactoryUrl factory) {
        try {
            VirtualFileSystem vfs = vfsRegistry.getProvider(EnvironmentContext.getCurrent().getWorkspaceId()).newInstance(null, null);
            Workspace workspace = workspaceManager.getWorkspaceByName(EnvironmentContext.getCurrent().getWorkspaceName());
            if (isRepositoryPublic(factory.getVcsurl())) {
                workspace.setAttribute("is_private", "false");
            } else {
                workspace.setAttribute("is_private", "true");

                Principal principal = DtoFactory.getInstance().createDto(Principal.class);
                principal.setType(Principal.Type.GROUP);
                principal.setName("workspace/developer");

                AccessControlEntry accessEntry = DtoFactory.getInstance().createDto(AccessControlEntry.class);
                accessEntry.setPermissions(Arrays.asList(VirtualFileSystemInfo.BasicPermissions.ALL.value()));
                accessEntry.setPrincipal(principal);

                Item project = vfs.getItemByPath("/" + factory.getProjectattributes().get(Param.PROFILE_ATTRIBUTES_PNAME.toString()),
                                                 null, true, PropertyFilter.ALL_FILTER);
                vfs.updateACL(project.getId(), Arrays.asList(accessEntry), true, null);

                workspaceManager.updateWorkspace(workspace);
                pushMessageToClient("We detected that you have been cloned private repository." +
                                    "Your current workspace now have private access.");
            }
        } catch (VirtualFileSystemException | OrganizationServiceException e) {
            LOG.warn("Failed to set workspace attributes.", e);
            pushMessageToClient("Failed to set up private property to current workspace.");
        }
    }

    private boolean isRepositoryPublic(String vcsUrl) {
        //check if url is ssh convert it to https
        if (vcsUrl.matches("((((git|ssh)://)(([^\\\\/@:]+@)??)[^\\\\/@:]+)|([^\\\\/@:]+@[^\\\\/@:]+))(:|/)[^\\\\@:]+")) {
            int separatorPos;
            if ((separatorPos = vcsUrl.indexOf("://")) != -1) {
                vcsUrl = vcsUrl.substring(separatorPos + 3);
            }
            if ((separatorPos = vcsUrl.indexOf('@')) != -1) {
                vcsUrl = vcsUrl.substring(separatorPos + 1);
            }
            vcsUrl = vcsUrl.replace(":", "/");
            vcsUrl = "https://".concat(vcsUrl);
        }

        try (InputStream ignored = new URL(vcsUrl).openStream()) {
            return true;
        } catch (IOException e) {
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(new URL(vcsUrl + "/info/refs?service=git-upload-pack").openStream()))) {

                String readerLine;
                while ((readerLine = reader.readLine()) != null) {
                    if (readerLine.contains("git repository not found")) {
                        return false;
                    }
                }

                return true;
            } catch (IOException e1) {
                LOG.warn("Failed to check repository status for url " + vcsUrl, e1);
                return false;
            }
        }
    }

    private void setDefaultProjectNameIfNeed(AdvancedFactoryUrl factory) {
        if (factory.getProjectattributes().get(Param.PROFILE_ATTRIBUTES_PNAME.toString()) == null
            || factory.getProjectattributes().get(Param.PROFILE_ATTRIBUTES_PNAME.toString()).trim().isEmpty()) {
            factory.getProjectattributes().put(Param.PROFILE_ATTRIBUTES_PNAME.toString(), NameGenerator.generate("Untitled_", 5));
        }
    }
}
