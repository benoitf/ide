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
package com.codenvy.ide.ext.git.client.push;

import com.codenvy.ide.ext.git.client.GitLocalizationConstant;
import com.codenvy.ide.job.Job;
import com.codenvy.ide.job.JobChangeEvent;
import com.codenvy.ide.job.RequestStatusHandlerBase;
import com.google.web.bindery.event.shared.EventBus;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id: Oct 31, 2011 evgen $
 */
public class PushRequestHandler extends RequestStatusHandlerBase {
    private String                  localBranch;
    private String                  remoteBranch;
    private GitLocalizationConstant constant;

    /**
     * Create handler.
     *
     * @param projectName
     * @param refSpec
     * @param eventBus
     * @param constant
     */
    public PushRequestHandler(@NotNull String projectName, @NotNull List<String> refSpec, @NotNull EventBus eventBus,
                              @NotNull GitLocalizationConstant constant) {
        super(projectName, eventBus);
        String[] split = refSpec.get(0).split(":");
        this.localBranch = split[0];
        this.remoteBranch = split[1];
        this.constant = constant;
    }

    /** {@inheritDoc} */
    @Override
    public void requestInProgress(String id) {
        Job job = new Job(id, Job.JobStatus.STARTED);
        job.setStartMessage(constant.pushStarted(projectName, localBranch, remoteBranch));
        eventBus.fireEvent(new JobChangeEvent(job));
    }

    /** {@inheritDoc} */
    @Override
    public void requestFinished(String id) {
        Job job = new Job(id, Job.JobStatus.FINISHED);
        job.setFinishMessage(constant.pushFinished(projectName, localBranch, remoteBranch));
        eventBus.fireEvent(new JobChangeEvent(job));
    }
}