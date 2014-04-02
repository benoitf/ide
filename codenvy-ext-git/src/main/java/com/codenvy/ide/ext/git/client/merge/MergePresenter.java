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
package com.codenvy.ide.ext.git.client.merge;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.commons.exception.ExceptionThrownEvent;
import com.codenvy.ide.ext.git.client.GitServiceClient;
import com.codenvy.ide.ext.git.client.GitLocalizationConstant;
import com.codenvy.ide.ext.git.shared.Branch;
import com.codenvy.ide.ext.git.shared.MergeResult;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.codenvy.ide.api.notification.Notification.Type.ERROR;
import static com.codenvy.ide.api.notification.Notification.Type.INFO;
import static com.codenvy.ide.ext.git.client.merge.Reference.RefType.LOCAL_BRANCH;
import static com.codenvy.ide.ext.git.client.merge.Reference.RefType.REMOTE_BRANCH;
import static com.codenvy.ide.ext.git.shared.BranchListRequest.LIST_LOCAL;
import static com.codenvy.ide.ext.git.shared.BranchListRequest.LIST_REMOTE;
import static com.codenvy.ide.ext.git.shared.MergeResult.MergeStatus.ALREADY_UP_TO_DATE;

/**
 * Presenter to perform merge reference with current HEAD commit.
 *
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 */
@Singleton
public class MergePresenter implements MergeView.ActionDelegate {
    public static final String LOCAL_BRANCHES_TITLE  = "Local Branches";
    public static final String REMOTE_BRANCHES_TITLE = "Remote Branches";
    private final DtoUnmarshallerFactory  dtoUnmarshallerFactory;
    private       MergeView               view;
    private       GitServiceClient        service;
    private       ResourceProvider        resourceProvider;
    private       EventBus                eventBus;
    private       GitLocalizationConstant constant;
    private       NotificationManager     notificationManager;
    private       Reference               selectedReference;
    private       String                  projectId;
    private       String                  projectName;

    /**
     * Create presenter.
     *
     * @param view
     * @param service
     * @param resourceProvider
     * @param eventBus
     * @param constant
     * @param notificationManager
     */
    @Inject
    public MergePresenter(MergeView view, GitServiceClient service, ResourceProvider resourceProvider, EventBus eventBus,
                          GitLocalizationConstant constant, NotificationManager notificationManager,
                          DtoUnmarshallerFactory dtoUnmarshallerFactory) {
        this.view = view;
        this.dtoUnmarshallerFactory = dtoUnmarshallerFactory;
        this.view.setDelegate(this);
        this.service = service;
        this.resourceProvider = resourceProvider;
        this.eventBus = eventBus;
        this.constant = constant;
        this.notificationManager = notificationManager;
    }

    /** Show dialog. */
    public void showDialog() {
        Project project = resourceProvider.getActiveProject();
        projectId = project.getId();
        projectName = project.getName();
        selectedReference = null;
        view.setEnableMergeButton(false);

        service.branchList(projectId, LIST_LOCAL,
                           new AsyncRequestCallback<Array<Branch>>(dtoUnmarshallerFactory.newArrayUnmarshaller(Branch.class)) {
                               @Override
                               protected void onSuccess(Array<Branch> result) {
                                   if (result.isEmpty()) {
                                       return;
                                   }

                                   Array<Reference> references = Collections.createArray();
                                   for (int i = 0; i < result.size(); i++) {
                                       Branch branch = result.get(i);
                                       if (!branch.isActive()) {
                                           Reference reference = new Reference(branch.getName(), branch.getDisplayName(), LOCAL_BRANCH);
                                           references.add(reference);
                                       }
                                   }
                                   view.setLocalBranches(references);
                               }

                               @Override
                               protected void onFailure(Throwable exception) {
                                   eventBus.fireEvent(new ExceptionThrownEvent(exception));
                                   Notification notification = new Notification(exception.getMessage(), ERROR);
                                   notificationManager.showNotification(notification);
                               }
                           });

        service.branchList(projectId, LIST_REMOTE,
                           new AsyncRequestCallback<Array<Branch>>(dtoUnmarshallerFactory.newArrayUnmarshaller(Branch.class)) {
                               @Override
                               protected void onSuccess(Array<Branch> result) {
                                   if (result.isEmpty()) {
                                       return;
                                   }

                                   Array<Reference> references = Collections.createArray();
                                   for (int i = 0; i < result.size(); i++) {
                                       Branch branch = result.get(i);
                                       if (!branch.isActive()) {
                                           Reference reference =
                                                   new Reference(branch.getName(), branch.getDisplayName(), REMOTE_BRANCH);
                                           references.add(reference);
                                       }
                                   }
                                   view.setRemoteBranches(references);
                               }

                               @Override
                               protected void onFailure(Throwable exception) {
                                   eventBus.fireEvent(new ExceptionThrownEvent(exception));
                                   Notification notification = new Notification(exception.getMessage(), ERROR);
                                   notificationManager.showNotification(notification);
                               }
                           });

        view.showDialog();
    }

    /** {@inheritDoc} */
    @Override
    public void onCancelClicked() {
        view.close();
    }

    /** {@inheritDoc} */
    @Override
    public void onMergeClicked() {
        service.merge(projectId, selectedReference.getDisplayName(),
                      new AsyncRequestCallback<MergeResult>(dtoUnmarshallerFactory.newUnmarshaller(MergeResult.class)) {
                          @Override
                          protected void onSuccess(final MergeResult result) {
                              resourceProvider.getProject(projectName, new AsyncCallback<Project>() {
                                  @Override
                                  public void onSuccess(Project project) {
                                      Notification notification = new Notification(formMergeMessage(result), INFO);
                                      notificationManager.showNotification(notification);
                                      view.close();
                                  }

                                  @Override
                                  public void onFailure(Throwable caught) {
                                      Log.error(MergePresenter.class, "can not get project " + projectName);
                                  }
                              });
                          }

                          @Override
                          protected void onFailure(Throwable exception) {
                              eventBus.fireEvent(new ExceptionThrownEvent(exception));
                              Notification notification = new Notification(exception.getMessage(), ERROR);
                              notificationManager.showNotification(notification);
                          }
                      });
    }

    /**
     * Form the result message of the merge operation.
     *
     * @param mergeResult
     *         result of merge operation
     * @return {@link String} merge result message
     */
    @NotNull
    private String formMergeMessage(@NotNull MergeResult mergeResult) {
        if (mergeResult.getMergeStatus().equals(ALREADY_UP_TO_DATE)) {
            return mergeResult.getMergeStatus().getValue();
        }

        StringBuilder conflictMessage = new StringBuilder();
        List<String> conflicts = mergeResult.getConflicts();
        if (conflicts != null && conflicts.size() > 0) {
            for (String conflict : conflicts) {
                conflictMessage.append("- ").append(conflict).append("<br>");
            }
        }
        StringBuilder commitsMessage = new StringBuilder();
        List<String> commits = mergeResult.getMergedCommits();
        if (commits != null && commits.size() > 0) {
            for (String commit : commits) {
                commitsMessage.append("- ").append(commit).append("<br>");
            }
        }

        String message = "<b>" + mergeResult.getMergeStatus().getValue() + "</b><br/>";
        String conflictText = conflictMessage.toString();
        message += (!conflictText.isEmpty()) ? constant.mergedConflicts(conflictText) : "";
        String commitText = commitsMessage.toString();
        message += (!commitText.isEmpty()) ? constant.mergedCommits(commitText) : "";
        message += (mergeResult.getNewHead() != null) ? constant.mergedNewHead(mergeResult.getNewHead()) : "";
        return message;
    }

    /** {@inheritDoc} */
    @Override
    public void onReferenceSelected(@NotNull Reference reference) {
        selectedReference = reference;
        String displayName = selectedReference.getDisplayName();
        boolean isEnabled = !displayName.equals(LOCAL_BRANCHES_TITLE) && !displayName.equals(REMOTE_BRANCHES_TITLE);
        view.setEnableMergeButton(isEnabled);
    }
}