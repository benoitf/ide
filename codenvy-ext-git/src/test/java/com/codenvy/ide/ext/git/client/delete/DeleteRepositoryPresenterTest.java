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
package com.codenvy.ide.ext.git.client.delete;

import com.codenvy.ide.api.event.RefreshBrowserEvent;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.commons.exception.ExceptionThrownEvent;
import com.codenvy.ide.ext.git.client.BaseTest;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Testing {@link DeleteRepositoryPresenter} functionality.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
public class DeleteRepositoryPresenterTest extends BaseTest {
    private DeleteRepositoryPresenter presenter;

    @Override
    public void disarm() {
        super.disarm();

        presenter = new DeleteRepositoryPresenter(service, eventBus, constant, resourceProvider, notificationManager);
    }

    @Test
    public void testDeleteRepositoryWhenDeleteRepositoryIsSuccessful() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                AsyncRequestCallback<Void> callback = (AsyncRequestCallback<Void>)arguments[2];
                Method onSuccess = GwtReflectionUtils.getMethod(callback.getClass(), "onSuccess");
                onSuccess.invoke(callback, (Void)null);
                return callback;
            }
        }).when(service).deleteRepository(anyString(), anyString(), (AsyncRequestCallback<Void>)anyObject());
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                AsyncCallback<Project> callback = (AsyncCallback<Project>)arguments[0];
                Method onSuccess = GwtReflectionUtils.getMethod(callback.getClass(), "onSuccess");
                onSuccess.invoke(callback, project);
                return callback;
            }
        }).when(project).refreshProperties((AsyncCallback<Project>)anyObject());

        presenter.deleteRepository();

        verify(resourceProvider).getActiveProject();
        verify(project).getPath();
        verify(service).deleteRepository(eq(VFS_ID), eq(PROJECT_ID), (AsyncRequestCallback<Void>)anyObject());
        verify(notificationManager).showNotification((Notification)anyObject());
        verify(constant).deleteGitRepositorySuccess();
        verify(eventBus).fireEvent((RefreshBrowserEvent)anyObject());
    }

    @Test
    public void testDeleteRepositoryWhenDeleteRepositoryIsFailed() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                AsyncRequestCallback<Void> callback = (AsyncRequestCallback<Void>)arguments[2];
                Method onFailure = GwtReflectionUtils.getMethod(callback.getClass(), "onFailure");
                onFailure.invoke(callback, mock(Throwable.class));
                return callback;
            }
        }).when(service).deleteRepository(anyString(), anyString(), (AsyncRequestCallback<Void>)anyObject());

        presenter.deleteRepository();

        verify(resourceProvider).getActiveProject();
        verify(project).getPath();
        verify(service).deleteRepository(eq(VFS_ID), eq(PROJECT_ID), (AsyncRequestCallback<Void>)anyObject());
        verify(notificationManager).showNotification((Notification)anyObject());
        verify(eventBus).fireEvent((ExceptionThrownEvent)anyObject());
    }

}