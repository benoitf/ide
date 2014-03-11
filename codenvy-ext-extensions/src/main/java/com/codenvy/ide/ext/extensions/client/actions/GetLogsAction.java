/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2013] Codenvy, S.A. 
 *  All Rights Reserved.
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
package com.codenvy.ide.ext.extensions.client.actions;

import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.ext.extensions.client.ExtRuntimeLocalizationConstant;
import com.codenvy.ide.ext.extensions.client.ExtRuntimeResources;
import com.codenvy.ide.ext.extensions.client.ExtensionsController;
import com.codenvy.ide.ext.extensions.shared.Constants;
import com.codenvy.ide.resources.model.Project;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Action to get logs of previously launched Codenvy extension.
 *
 * @author Artem Zatsarynnyy
 */
@Singleton
public class GetLogsAction extends Action {

    private final ResourceProvider     resourceProvider;
    private       ExtensionsController controller;

    @Inject
    public GetLogsAction(ExtensionsController controller, ExtRuntimeResources resources,
                         ResourceProvider resourceProvider, ExtRuntimeLocalizationConstant localizationConstants) {
        super(localizationConstants.getExtensionLogsActionText(),
              localizationConstants.getExtensionLogsActionDescription(), resources.getExtensionLogs());
        this.controller = controller;
        this.resourceProvider = resourceProvider;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        controller.getLogs();
    }

    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        Project activeProject = resourceProvider.getActiveProject();
        if (activeProject != null) {
            e.getPresentation().setVisible(activeProject.getDescription().getProjectTypeId().equals(Constants.CODENVY_EXTENSION_ID) ||
                                           activeProject.getDescription().getProjectTypeId().equals("codenvy_tutorial"));
            e.getPresentation().setEnabled(controller.isAnyAppLaunched());
        } else {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}
