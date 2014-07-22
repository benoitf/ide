/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.actions;

import com.codenvy.api.analytics.logger.AnalyticsEventLogger;
import com.codenvy.ide.Resources;
import com.codenvy.ide.api.event.ProjectActionEvent;
import com.codenvy.ide.api.resources.ProjectsManager;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/** @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a> */
@Singleton
public class CloseProjectAction extends Action {

    private final ProjectsManager      projectsManager;
    private final AnalyticsEventLogger eventLogger;
    private final EventBus             eventBus;

    @Inject
    public CloseProjectAction(ProjectsManager projectsManager,
                              Resources resources,
                              AnalyticsEventLogger eventLogger,
                              EventBus eventBus) {
        super("Close Project", "Close project", null, resources.closeProject());
        this.projectsManager = projectsManager;
        this.eventLogger = eventLogger;
        this.eventBus = eventBus;
    }

    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        e.getPresentation().setVisible(projectsManager.getActiveProject() != null);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        eventLogger.log("IDE: Close project");

        if (projectsManager.getActiveProject() != null) {
            eventBus.fireEvent(ProjectActionEvent.createProjectClosedEvent(projectsManager.getActiveProject()));
        }
    }
}
