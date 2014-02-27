package com.codenvy.ide.factory.server;

import com.codenvy.api.factory.dto.AdvancedFactoryUrl;
import com.codenvy.api.vfs.server.exceptions.VirtualFileSystemException;
import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.git.server.GitException;
import com.codenvy.ide.ext.git.server.rest.GitService;
import com.codenvy.ide.ext.git.shared.BranchCheckoutRequest;
import com.codenvy.ide.ext.git.shared.CloneRequest;
import com.codenvy.ide.ext.git.shared.RepoInfo;
import com.codenvy.ide.factory.server.rest.FactoryException;
import com.codenvy.organization.client.WorkspaceManager;

import org.codenvy.mail.MailSenderClient;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * @author vzhukovskii@codenvy.com
 */
@Listeners(value = MockitoTestNGListener.class)
public class FactoryTest {
    @Mock
    private MailSenderClient mailClient;

    @Mock
    private GitService gitService;

    @Mock
    private WorkspaceManager workspaceManager;

    @InjectMocks
    private Factory factory;

    @Test
    public void shouldAcceptFactory() throws Exception {
        AdvancedFactoryUrl factory = DtoFactory.getInstance().createDto(AdvancedFactoryUrl.class);

        when(gitService.clone((CloneRequest)any())).thenReturn(DtoFactory.getInstance().createDto(RepoInfo.class));

        this.factory.acceptFactory(factory);
    }

    @DataProvider(name = "cloneExceptions")
    public Object[][] exceptionsSet() {
        return new Object[][]{
                new Object[]{new URISyntaxException("input", "reason")},
                new Object[]{new VirtualFileSystemException("message")},
                new Object[]{new GitException("message")}
        };
    }

    @Test(dataProvider = "cloneExceptions",
          expectedExceptionsMessageRegExp = "Failed to accept factory",
          expectedExceptions = {FactoryException.class})
    public void shouldThrowExceptionsWhenAcceptFactoryOnCloneProjectStep(Throwable exception) throws Exception {
        AdvancedFactoryUrl factory = DtoFactory.getInstance().createDto(AdvancedFactoryUrl.class);

        when(gitService.clone((CloneRequest)any())).thenThrow(exception);

        this.factory.acceptFactory(factory);
    }

    @Test
    public void shouldInvokeOneTimeCheckoutMethodWhenTriesToCheckoutToCommitId() throws Exception {
        AdvancedFactoryUrl factory = DtoFactory.getInstance().createDto(AdvancedFactoryUrl.class);
        factory.setCommitid("commitId");

        final BranchCheckoutRequest checkoutRequest = DtoFactory.getInstance().createDto(BranchCheckoutRequest.class);
        checkoutRequest.setCreateNew(true);
        checkoutRequest.setName("temp");
        checkoutRequest.setStartPoint(factory.getCommitid());

        when(gitService.clone((CloneRequest)any())).thenReturn(DtoFactory.getInstance().createDto(RepoInfo.class));

        this.factory.acceptFactory(factory);

        ArgumentCaptor<BranchCheckoutRequest> argument = ArgumentCaptor.forClass(BranchCheckoutRequest.class);
        verify(gitService).branchCheckout(argument.capture());
        assertEquals(checkoutRequest.getStartPoint(), argument.getValue().getStartPoint());
        assertEquals(checkoutRequest.getName(), argument.getValue().getName());
        assertEquals(checkoutRequest.isCreateNew(), argument.getValue().isCreateNew());
    }

    @DataProvider(name = "commitIdCheckoutExceptions")
    public Object[][] exceptionsSetCheckoutId() {
        return new Object[][] {
                new Object[] {new VirtualFileSystemException("message")},
                new Object[] {new GitException("message")}
        };
    }

    @Test(dataProvider = "cloneExceptions",
          expectedExceptionsMessageRegExp = "Failed to accept factory",
          expectedExceptions = {FactoryException.class})
    public void shouldThrowExceptionsWhenTryToCheckoutToCommitId() throws Exception {
        AdvancedFactoryUrl factory = DtoFactory.getInstance().createDto(AdvancedFactoryUrl.class);
        factory.setCommitid("commitId");

        final BranchCheckoutRequest checkoutRequest = DtoFactory.getInstance().createDto(BranchCheckoutRequest.class);
        checkoutRequest.setCreateNew(true);
        checkoutRequest.setName("temp");
        checkoutRequest.setStartPoint(factory.getCommitid());
    }
}
