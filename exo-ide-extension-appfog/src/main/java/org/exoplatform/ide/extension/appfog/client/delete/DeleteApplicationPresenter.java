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
package org.exoplatform.ide.extension.appfog.client.delete;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.ui.HasValue;
import com.google.web.bindery.autobean.shared.AutoBean;

import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.rest.AutoBeanUnmarshaller;
import org.exoplatform.gwtframework.ui.client.api.TextFieldItem;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.output.event.OutputEvent;
import org.exoplatform.ide.client.framework.output.event.OutputMessage.Type;
import org.exoplatform.ide.client.framework.ui.api.IsView;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedEvent;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler;
import org.exoplatform.ide.extension.appfog.client.AppfogAsyncRequestCallback;
import org.exoplatform.ide.extension.appfog.client.AppfogClientService;
import org.exoplatform.ide.extension.appfog.client.AppfogExtension;
import org.exoplatform.ide.extension.appfog.client.login.LoggedInHandler;
import org.exoplatform.ide.extension.appfog.shared.AppfogApplication;
import org.exoplatform.ide.git.client.GitPresenter;
import org.exoplatform.ide.vfs.client.model.ProjectModel;

/**
 * Presenter for delete application operation.
 *
 * @author <a href="mailto:vzhukovskii@exoplatform.com">Vladislav Zhukovskii</a>
 * @version $Id: $
 */
public class DeleteApplicationPresenter extends GitPresenter implements DeleteApplicationHandler, ViewClosedHandler {
    interface Display extends IsView {
        /**
         * Get delete services checkbox field.
         *
         * @return {@link TextFieldItem}
         */
        HasValue<Boolean> getDeleteServicesCheckbox();

        /**
         * Get delete button's click handler.
         *
         * @return {@link HasClickHandlers} click handler
         */
        HasClickHandlers getDeleteButton();

        /**
         * Get cancel button's click handler.
         *
         * @return {@link HasClickHandlers} click handler
         */
        HasClickHandlers getCancelButton();

        /**
         * Set the ask message to delete application.
         *
         * @param message
         */
        void setAskMessage(String message);

        void setAskDeleteServices(String text);
    }

    private Display display;

    /** The name of application. */
    private String appName;

    /** Name of the server. */
    private String serverName;

    public DeleteApplicationPresenter() {
        IDE.addHandler(DeleteApplicationEvent.TYPE, this);
        IDE.addHandler(ViewClosedEvent.TYPE, this);
    }

    public void bindDisplay() {
        display.getCancelButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                closeView();
            }
        });

        display.getDeleteButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                deleteApplication();
            }
        });
    }

    @Override
    public void onDeleteApplication(DeleteApplicationEvent event) {
        serverName = event.getServer();
        if (event.getApplicationName() == null && makeSelectionCheck()) {
            getApplicationInfo();
        } else {
            appName = event.getApplicationName();
            showDeleteDialog(appName);
        }
    }

    private LoggedInHandler appInfoLoggedInHandler = new LoggedInHandler() {
        @Override
        public void onLoggedIn() {
            getApplicationInfo();
        }
    };

    private void getApplicationInfo() {
//      String projectId = ((ItemContext)selectedItems.get(0)).getProject().getId();
        String projectId = getSelectedProject().getId();

        try {
            AutoBean<AppfogApplication> appfogApplication =
                    AppfogExtension.AUTO_BEAN_FACTORY.appfogApplication();

            AutoBeanUnmarshaller<AppfogApplication> unmarshaller =
                    new AutoBeanUnmarshaller<AppfogApplication>(appfogApplication);

            AppfogClientService.getInstance().getApplicationInfo(vfs.getId(), projectId, null, null,
                                                                 new AppfogAsyncRequestCallback<AppfogApplication>(unmarshaller,
                                                                                                                   appInfoLoggedInHandler,
                                                                                                                   null) {
                                                                     @Override
                                                                     protected void onSuccess(AppfogApplication result) {
                                                                         appName = result.getName();
                                                                         showDeleteDialog(appName);
                                                                     }
                                                                 });
        } catch (RequestException e) {
            IDE.fireEvent(new ExceptionThrownEvent(e));
        }
    }

    private LoggedInHandler deleteAppLoggedInHandler = new LoggedInHandler() {
        @Override
        public void onLoggedIn() {
            deleteApplication();
        }
    };

    private void deleteApplication() {
        boolean isDeleteServices = display.getDeleteServicesCheckbox().getValue();
        String projectId = null;

//      if (selectedItems.size() > 0 && selectedItems.get(0) instanceof ItemContext)
//      {
//         ProjectModel project = ((ItemContext)selectedItems.get(0)).getProject();
//         if (project != null && project.getPropertyValue("appfog-application") != null
//            && appName.equals((String)project.getPropertyValue("appfog-application")))
//         {
//            projectId = project.getId();
//         }
//      }

        if (selectedItem != null) {
            ProjectModel project = getSelectedProject();
            if (project != null && project.getPropertyValue("appfog-application") != null
                && appName.equals((String)project.getPropertyValue("appfog-application"))) {
                projectId = project.getId();
            }
        }

        try {
            AppfogClientService.getInstance().deleteApplication(vfs.getId(), projectId, appName, serverName,
                                                                isDeleteServices,
                                                                new AppfogAsyncRequestCallback<String>(null, deleteAppLoggedInHandler,
                                                                                                       null) {
                                                                    @Override
                                                                    protected void onSuccess(String result) {
                                                                        closeView();
                                                                        IDE.fireEvent(new OutputEvent(AppfogExtension.LOCALIZATION_CONSTANT
                                                                                                                     .applicationDeletedMsg(
                                                                                                                             appName),
                                                                                                      Type.INFO));
                                                                        IDE.fireEvent(new ApplicationDeletedEvent(appName));
                                                                    }
                                                                });
        } catch (RequestException e) {
            IDE.fireEvent(new ExceptionThrownEvent(e));
        }
    }

    private void showDeleteDialog(String appName) {
        if (display == null) {
            display = GWT.create(Display.class);
            bindDisplay();
            IDE.getInstance().openView(display.asView());
            display.setAskMessage(AppfogExtension.LOCALIZATION_CONSTANT.deleteApplicationQuestion(appName));
            display.setAskDeleteServices(AppfogExtension.LOCALIZATION_CONSTANT.deleteApplicationAskDeleteServices());
        }
    }

    private void closeView() {
        IDE.getInstance().closeView(display.asView().getId());
    }

    /** @see org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler#onViewClosed(org.exoplatform.ide.client.framework.ui.api.event.ViewClosedEvent) */
    @Override
    public void onViewClosed(ViewClosedEvent event) {
        if (event.getView() instanceof Display) {
            display = null;
        }
    }

}