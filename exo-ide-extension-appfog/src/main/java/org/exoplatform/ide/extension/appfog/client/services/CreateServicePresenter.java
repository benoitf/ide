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
package org.exoplatform.ide.extension.appfog.client.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.ui.HasValue;
import com.google.web.bindery.autobean.shared.AutoBean;

import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.gwtframework.commons.rest.AutoBeanUnmarshaller;
import org.exoplatform.gwtframework.ui.client.dialog.Dialogs;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.ui.api.IsView;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedEvent;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler;
import org.exoplatform.ide.extension.appfog.client.AppfogAsyncRequestCallback;
import org.exoplatform.ide.extension.appfog.client.AppfogClientService;
import org.exoplatform.ide.extension.appfog.client.AppfogExtension;
import org.exoplatform.ide.extension.appfog.client.login.LoggedInHandler;
import org.exoplatform.ide.extension.appfog.shared.AppfogProvisionedService;
import org.exoplatform.ide.extension.appfog.shared.AppfogServices;
import org.exoplatform.ide.extension.appfog.shared.AppfogSystemService;
import org.exoplatform.ide.git.client.GitPresenter;
import org.exoplatform.ide.vfs.client.model.ProjectModel;

import java.util.LinkedHashMap;

/**
 * Presenter for creating new service.
 *
 * @author <a href="mailto:vzhukovskii@exoplatform.com">Vladislav Zhukovskii</a>
 * @version $Id: $
 */
public class CreateServicePresenter extends GitPresenter implements CreateServiceHandler, ViewClosedHandler {
    interface Display extends IsView {
        HasValue<String> getSystemServicesField();

        HasValue<String> getNameField();

        HasClickHandlers getCreateButton();

        HasClickHandlers getCancelButton();

        void setServices(LinkedHashMap<String, String> values);
    }

    /** Display. */
    private Display display;

    /** Handler for successful service creation. */
    private ProvisionedServiceCreatedHandler serviceCreatedHandler;

    private LoggedInHandler createServiceLoggedInHandler = new LoggedInHandler() {

        @Override
        public void onLoggedIn() {
            doCreate();
        }
    };

    public CreateServicePresenter() {
        IDE.addHandler(CreateServiceEvent.TYPE, this);
        IDE.addHandler(ViewClosedEvent.TYPE, this);
    }

    public void bindDisplay() {
        display.getCancelButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                IDE.getInstance().closeView(display.asView().getId());
            }
        });

        display.getCreateButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                doCreate();
            }
        });
    }

    /** Get the list of CloudFoundry services (provisioned and system). */
    private void getServices() {
        try {
            AppfogClientService.getInstance().services(AppfogExtension.DEFAULT_SERVER,
                                                       new AsyncRequestCallback<AppfogServices>(new AppfogServicesUnmarshaller()) {

                                                           @Override
                                                           protected void onSuccess(AppfogServices result) {
                                                               LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
                                                               for (AppfogSystemService service : result.getAppfogSystemService()) {
                                                                   values.put(service.getVendor(), service.getDescription());
                                                               }
                                                               display.setServices(values);
                                                           }

                                                           @Override
                                                           protected void onFailure(Throwable exception) {
                                                               Dialogs.getInstance().showError(
                                                                       AppfogExtension.LOCALIZATION_CONSTANT.retrieveServicesFailed());
                                                           }
                                                       });
        } catch (RequestException e) {
            IDE.fireEvent(new ExceptionThrownEvent(e));
        }
    }

    /** @see org.exoplatform.ide.extension.cloudfoundry.client.services.CreateServiceHandler#onCreateService(org.exoplatform.ide
     * .extension.cloudfoundry.client.services.CreateServiceEvent) */
    @Override
    public void onCreateService(CreateServiceEvent event) {
        this.serviceCreatedHandler = event.getProvisionedServiceCreatedHandler();
        if (display == null) {
            display = GWT.create(Display.class);
            IDE.getInstance().openView(display.asView());
            bindDisplay();
        }
        getServices();
    }

    /** @see org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler#onViewClosed(org.exoplatform.ide.client.framework.ui.api
     * .event.ViewClosedEvent) */
    @Override
    public void onViewClosed(ViewClosedEvent event) {
        if (event.getView() instanceof Display) {
            display = null;
        }
    }

    /** Create new provisioned service. */
    private void doCreate() {
        String name = display.getNameField().getValue();
        String type = display.getSystemServicesField().getValue();

//      final ProjectModel project = ((ItemContext)selectedItems.get(0)).getProject();
        final ProjectModel project = getSelectedProject();
        final String infraName = project.getProperty("appfog-infra").getValue().get(0);

        try {
            AutoBean<AppfogProvisionedService> provisionedService = AppfogExtension.AUTO_BEAN_FACTORY.provisionedService();
            AutoBeanUnmarshaller<AppfogProvisionedService> unmarshaller =
                    new AutoBeanUnmarshaller<AppfogProvisionedService>(provisionedService);

            AppfogClientService.getInstance().createService(AppfogExtension.DEFAULT_SERVER, type, name, null, null, null, infraName,
                                                            new AppfogAsyncRequestCallback<AppfogProvisionedService>(unmarshaller,



                                                                                                                     createServiceLoggedInHandler,
                                                                                                                     null) {

                                                                @Override
                                                                protected void onSuccess(AppfogProvisionedService result) {
                                                                    IDE.getInstance().closeView(display.asView().getId());
                                                                    serviceCreatedHandler.onProvisionedServiceCreated(result);
                                                                }
                                                            });
        } catch (RequestException e) {
            IDE.fireEvent(new ExceptionThrownEvent(e));
        }
    }
}
