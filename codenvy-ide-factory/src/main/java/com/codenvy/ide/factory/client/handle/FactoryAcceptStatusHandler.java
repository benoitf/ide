package com.codenvy.ide.factory.client.handle;

import com.codenvy.ide.factory.client.FactoryLocalizationConstant;
import com.codenvy.ide.job.Job;
import com.codenvy.ide.job.JobChangeEvent;
import com.codenvy.ide.rest.RequestStatusHandler;
import com.google.web.bindery.event.shared.EventBus;

import javax.validation.constraints.NotNull;

/**
 * @author vzhukovskii@codenvy.com
 */
public class FactoryAcceptStatusHandler implements RequestStatusHandler {

    private EventBus                    eventBus;
    private FactoryLocalizationConstant constant;

    public FactoryAcceptStatusHandler(@NotNull EventBus eventBus, @NotNull FactoryLocalizationConstant constant) {
        super();
        this.eventBus = eventBus;
        this.constant = constant;
    }

    @Override
    public void requestInProgress(String id) {
        Job job = new Job(id, Job.JobStatus.STARTED);
        job.setStartMessage("Cloning started"); //TODO use locale constant
        eventBus.fireEvent(new JobChangeEvent(job));
    }

    @Override
    public void requestFinished(String id) {
        Job job = new Job(id, Job.JobStatus.FINISHED);
        job.setFinishMessage("cloning finished"); //TODO use locale constant
        eventBus.fireEvent(new JobChangeEvent(job));
    }

    @Override
    public void requestError(String id, Throwable exception) {
        Job job = new Job(id, Job.JobStatus.ERROR);
        job.setError(exception);
        eventBus.fireEvent(new JobChangeEvent(job));
    }
}
