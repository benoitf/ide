/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package org.exoplatform.ide.git.server.rest;

import com.codenvy.commons.security.oauth.OAuthTokenProvider;
import com.codenvy.ide.commons.server.ParsingResponseException;

import org.exoplatform.ide.extension.ssh.server.SshKeyStoreException;
import org.exoplatform.ide.git.server.github.GitHub;
import org.exoplatform.ide.git.server.github.GitHubException;
import org.exoplatform.ide.git.shared.Collaborators;
import org.exoplatform.ide.git.shared.GitHubRepository;
import org.exoplatform.ide.git.shared.GitHubRepositoryList;
import org.exoplatform.ide.git.shared.GitHubUser;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * REST service to get the list of repositories from GitHub (where sample projects are located).
 * 
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id: GithubSamplesService.java Aug 29, 2011 9:59:02 AM vereshchaka $
 */
@Path("{ws-name}/github")
public class GitHubService {
    @Inject
    OAuthTokenProvider oauthTokenProvider;

    @Inject
    GitHub             github;

    public GitHubService() {
    }

    protected GitHubService(GitHub github) {
        this.github = github;
    }

    @Path("list/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GitHubRepositoryList listRepositoriesByUser(
                                                       @QueryParam("username") String userName) throws IOException,
                                                                                               GitHubException,
                                                                                               ParsingResponseException {
        return github.listUserPublicRepositories(userName);
    }
    
    @Path("list/org")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GitHubRepositoryList listRepositoriesByOrganization(
                                                               @QueryParam("organization") String organization) throws IOException,
                                                                                                               GitHubException,
                                                                                                               ParsingResponseException {
        return github.listAllOrganizationRepositories(organization);
    }
    
    @Path("list/account")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GitHubRepositoryList listRepositoriesByAccount(
                                                          @QueryParam("account") String account) throws IOException,
                                                                                                GitHubException,
                                                                                                ParsingResponseException {
        try {
            //First, try to retrieve organization repositories:
            return github.listAllOrganizationRepositories(account);
        } catch (GitHubException ghe) {
            //If account is not organization, then try by user name:
            if (ghe.getResponseStatus() == 404) {
                return github.listUserPublicRepositories(account);
            } else {
                throw ghe;
            }
        }
    }

    @Path("list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GitHubRepositoryList listRepositories() throws IOException, GitHubException, ParsingResponseException {
        return github.listCurrentUserRepositories();
    }
    

    @Path("list/available")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<GitHubRepository>> availableRepositories() throws IOException, GitHubException, ParsingResponseException
    {
        return github.availableRepositoriesList();
    }
    
    @Path("page")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GitHubRepositoryList getPage(@QueryParam("url") String url) throws IOException, GitHubException, ParsingResponseException {
        return github.getPage(url);
    }
    
    @Path("orgs")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> listOrganizations() throws IOException, GitHubException, ParsingResponseException
    {
        return github.listOrganizations();
    }
    
    @Path("user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GitHubUser getUserInfo() throws IOException, GitHubException, ParsingResponseException
    {
        return github.getGithubUser();
    }

    @GET
    @Path("collaborators/{user}/{repository}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collaborators collaborators(@PathParam("user") String user,
                                       @PathParam("repository") String repository) throws IOException,
                                                                                  GitHubException, ParsingResponseException {
        return github.getCollaborators(user, repository);
    }

    @GET
    @Path("token/{userid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getToken(@PathParam("userid") String userId) throws IOException, GitHubException, ParsingResponseException {
        return github.getToken(userId);
    }

    @POST
    @Path("ssh/generate")
    public void updateSSHKey() throws SshKeyStoreException, IOException, GitHubException, ParsingResponseException {
        github.generateGitHubSshKey();
    }
}
