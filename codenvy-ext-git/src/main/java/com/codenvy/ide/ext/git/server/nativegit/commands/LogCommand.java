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
package com.codenvy.ide.ext.git.server.nativegit.commands;

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.git.server.GitException;
import com.codenvy.ide.ext.git.shared.GitUser;
import com.codenvy.ide.ext.git.shared.Revision;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Show commit logs
 *
 * @author <a href="mailto:evoevodin@codenvy.com">Eugene Voevodin</a>
 */
public class LogCommand extends GitCommand<List<Revision>> {

    private int    count;
    private String branch;

    public LogCommand(File place) {
        super(place);
    }

    /** @see com.codenvy.ide.ext.git.server.nativegit.commands.GitCommand#execute() */
    @Override
    public List<Revision> execute() throws GitException {
        clear();
        commandLine.add("log")
                   .add("--format=%an#%ae#%cn#%ce#%cd#%H#%s")
                   .add("--date=raw");
        if (branch != null) {
            commandLine.add(branch);
        }
        if (count > 0) {
            commandLine.add("-" + count);
        }
        start();
        List<Revision> list = new LinkedList<>();
        for (String oneRev : getOutput()) {
            String[] elements = oneRev.split("#");
            GitUser committer = DtoFactory.getInstance().createDto(GitUser.class).withName(elements[2]).withEmail(elements[3]);
            long commitTime = Long.parseLong(elements[4].substring(0, elements[4].indexOf(" "))) * 1000L;
            String commitId = elements[5];
            StringBuilder commitMessage = new StringBuilder();
            for (int i = 6; i < elements.length; i++) {
                commitMessage.append(elements[i]);
            }
            Revision revision = DtoFactory.getInstance().createDto(Revision.class).withId(commitId).withMessage(commitMessage.toString()).withCommitTime(commitTime).withCommitter(committer);
            list.add(revision);
        }
        return list;
    }

    /**
     * @param count
     *         log objects limit
     * @return LogCommand with established limit of log objects
     * @throws GitException
     */
    public LogCommand setCount(int count) {
        this.count = count;
        return this;
    }

    /**
     * @param branch
     *         branch
     * @return LogCommand with established branch
     */
    public LogCommand setBranch(String branch) {
        this.branch = branch;
        return this;
    }
}
