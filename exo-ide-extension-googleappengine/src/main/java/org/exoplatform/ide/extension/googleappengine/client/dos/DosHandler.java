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
package org.exoplatform.ide.extension.googleappengine.client.dos;

import com.google.gwt.http.client.RequestException;

import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.ui.client.dialog.Dialogs;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.output.event.OutputEvent;
import org.exoplatform.ide.client.framework.output.event.OutputMessage.Type;
import org.exoplatform.ide.extension.googleappengine.client.GoogleAppEngineAsyncRequestCallback;
import org.exoplatform.ide.extension.googleappengine.client.GoogleAppEngineClientService;
import org.exoplatform.ide.extension.googleappengine.client.GoogleAppEngineExtension;
import org.exoplatform.ide.extension.googleappengine.client.GoogleAppEnginePresenter;

/**
 * @author <a href="mailto:azhuleva@exoplatform.com">Ann Shumilova</a>
 * @version $Id: May 24, 2012 5:00:19 PM anya $
 */
public class DosHandler extends GoogleAppEnginePresenter implements UpdateDosHandler {

    public DosHandler() {
        IDE.addHandler(UpdateDosEvent.TYPE, this);
    }

    /** @see org.exoplatform.ide.extension.googleappengine.client.queues.UpdateQueuesHandler#onUpdateQueues(org.exoplatform.ide.extension
     * .googleappengine.client.queues.UpdateQueuesEvent) */
    @Override
    public void onUpdateDos(UpdateDosEvent event) {
        if (isAppEngineProject()) {
            updateDos();
        } else {
            Dialogs.getInstance().showError(GoogleAppEngineExtension.GAE_LOCALIZATION.notAppEngineProjectError());
        }
    }

    public void updateDos() {
        try {
            GoogleAppEngineClientService.getInstance().updateDos(currentVfs.getId(), currentProject.getId(),
                                                                 new GoogleAppEngineAsyncRequestCallback<Object>() {

                                                                     @Override
                                                                     protected void onSuccess(Object result) {
                                                                         IDE.fireEvent(new OutputEvent(
                                                                                 GoogleAppEngineExtension.GAE_LOCALIZATION
                                                                                                         .updateDosSuccessfully(),
                                                                                 Type.INFO));
                                                                     }
                                                                 });
        } catch (RequestException e) {
            IDE.fireEvent(new ExceptionThrownEvent(e));
        }
    }
}