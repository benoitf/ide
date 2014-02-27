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
package com.codenvy.ide.ext.git.server;


import com.codenvy.commons.env.EnvironmentContext;
import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.git.shared.GitUser;
import com.codenvy.organization.client.UserManager;
import com.codenvy.organization.exception.OrganizationServiceException;
import com.codenvy.organization.model.User;
import com.google.inject.Inject;

import java.io.File;

/** @author andrew00x */
public abstract class GitConnectionFactory {

    @Inject
    UserManager userManager;

    /**
     * Get connection to Git repository located in <code>workDir</code>.
     *
     * @param workDir
     *         repository directory
     * @param user
     *         user
     * @return connection to Git repository
     * @throws GitException
     *         if can't initialize connection
     */
    public final GitConnection getConnection(String workDir, GitUser user) throws GitException {
        return getConnection(new File(workDir), user);
    }

    /**
     * Get connection to Git repository located in <code>workDir</code>.
     *
     * @param workDir
     *         repository directory
     * @param user
     *         user
     * @return connection to Git repository
     * @throws GitException
     *         if can't initialize connection
     */
    public abstract GitConnection getConnection(File workDir, GitUser user) throws GitException;

    public GitConnection getConnection(String workDir) throws GitException {
        GitUser gitUser = null;
        try {
            final String name = EnvironmentContext.getCurrent().getUser().getName();
            User user = userManager.getUserByAlias(name);
            String firstName = user.getProfile().getAttribute("firstName");
            String lastName = user.getProfile().getAttribute("lastName");
            String username = "";
            if (firstName != null && firstName.length() != 0) {
                username += firstName.concat(" ");
            }
            if (lastName != null && lastName.length() != 0) {
                username += lastName;
            }
            if (username.length() != 0) {
                gitUser = DtoFactory.getInstance().createDto(GitUser.class).withName(username).withEmail(name);
            } else {
                gitUser = DtoFactory.getInstance().createDto(GitUser.class).withName(name);
            }
        } catch (OrganizationServiceException e) {
            throw new GitException("User not found");
        }
        return getConnection(new File(workDir), gitUser);
    }
}