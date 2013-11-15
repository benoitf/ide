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
package com.codenvy.ide.ext.tutorials.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.template.TemplateAgent;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.wizard.template.AbstractTemplatePage;
import com.codenvy.ide.ext.tutorials.client.action.ShowTutorialGuideAction;
import com.codenvy.ide.ext.tutorials.client.template.*;
import com.codenvy.ide.json.JsonCollections;
import com.codenvy.ide.resources.ProjectTypeAgent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_WINDOW;
import static com.codenvy.ide.ext.java.client.projectmodel.JavaProject.PRIMARY_NATURE;

/**
 * Entry point for an extension that adds support to work with tutorial projects.
 *
 * @author <a href="mailto:azatsarynnyy@codenvy.com">Artem Zatsarynnyy</a>
 * @version $Id: TutorialsExtension.java Sep 13, 2013 4:14:56 PM azatsarynnyy $
 */
@Singleton
@Extension(title = "Codenvy tutorial projects support.", version = "3.0.0")
public class TutorialsExtension {
    /** Default name of the tutorial project type. */
    public static final String TUTORIAL_PROJECT_TYPE           = "CodenvyTutorial";
    /** Default name of the file that contains tutorial description. */
    public static final String DEFAULT_README_FILE_NAME        = "guide.html";
    public static final String DTO_TUTORIAL_ID                 = "DTOTutorial";
    public static final String ACTION_TUTORIAL_ID              = "ActionTutorial";
    public static final String NOTIFICATION_TUTORIAL_ID        = "NotificationTutorial";
    public static final String WIZARD_TUTORIAL_ID              = "WizardTutorial";
    public static final String NEW_PROJECT_WIZARD_TUTORIAL_ID  = "NewProjectWizardTutorial";
    public static final String NEW_RESOURCE_WIZARD_TUTORIAL_ID = "NewResourceWizardTutorial";
    public static final String PARTS_TUTORIAL_ID               = "PartsTutorial";

    @Inject
    public TutorialsExtension(TemplateAgent templateAgent,
                              Provider<CreateDTOTutorialPage> createDTOTutorialPage,
                              Provider<CreateActionTutorialPage> createActionTutorialPage,
                              Provider<CreateNotificationTutorialPage> createNotificationTutorialPage,
                              Provider<CreateWizardTutorialPage> createWizardTutorialPageProvider,
                              Provider<CreateNewProjectWizardTutorialPage> createNewProjectWizardTutorialPageProvider,
                              Provider<CreateNewResourceWizardTutorialPage> createNewResourceWizardTutorialPageProvider,
                              Provider<CreatePartsTutorialPage> createPartsTutorialPageProvider,
                              ProjectTypeAgent projectTypeAgent,
                              TutorialsResources resources,
                              TutorialsLocalizationConstant localizationConstants,
                              ActionManager actionManager,
                              ShowTutorialGuideAction showAction) {
        resources.tutorialsCss().ensureInjected();

        // register actions
        DefaultActionGroup windowMenuActionGroup = (DefaultActionGroup)actionManager.getAction(GROUP_WINDOW);

        actionManager.registerAction(localizationConstants.showTutorialGuideActionId(), showAction);
        windowMenuActionGroup.add(showAction);

        // register project type
        projectTypeAgent.register(TUTORIAL_PROJECT_TYPE,
                                  "Codenvy tutorial",
                                  resources.codenvyTutorialProject(),
                                  PRIMARY_NATURE,
                                  JsonCollections.<String>createArray(TUTORIAL_PROJECT_TYPE));

        // register templates
        templateAgent.register(DTO_TUTORIAL_ID,
                               "Tutorial project that illustrates examples of using DTO.",
                               resources.codenvyTutorialTemplate(),
                               PRIMARY_NATURE,
                               JsonCollections.createArray(TUTORIAL_PROJECT_TYPE),
                               JsonCollections.<Provider<? extends AbstractTemplatePage>>createArray(createDTOTutorialPage));
        templateAgent.register(NOTIFICATION_TUTORIAL_ID,
                               "Tutorial project that illustrates examples of using Notification API.",
                               resources.codenvyTutorialTemplate(),
                               PRIMARY_NATURE,
                               JsonCollections.createArray(TUTORIAL_PROJECT_TYPE),
                               JsonCollections.<Provider<? extends AbstractTemplatePage>>createArray(createNotificationTutorialPage));
        templateAgent.register(ACTION_TUTORIAL_ID,
                               "Tutorial project that illustrates examples of using Action API.",
                               resources.codenvyTutorialTemplate(),
                               PRIMARY_NATURE,
                               JsonCollections.createArray(TUTORIAL_PROJECT_TYPE),
                               JsonCollections.<Provider<? extends AbstractTemplatePage>>createArray(createActionTutorialPage));
        templateAgent.register(WIZARD_TUTORIAL_ID,
                               "Tutorial project that illustrates examples of using Wizard API.",
                               resources.codenvyTutorialTemplate(),
                               PRIMARY_NATURE,
                               JsonCollections.createArray(TUTORIAL_PROJECT_TYPE),
                               JsonCollections.<Provider<? extends AbstractTemplatePage>>createArray(createWizardTutorialPageProvider));
        templateAgent.register(NEW_PROJECT_WIZARD_TUTORIAL_ID,
                               "Tutorial project that illustrates examples of using New project wizard.",
                               resources.codenvyTutorialTemplate(),
                               PRIMARY_NATURE,
                               JsonCollections.createArray(TUTORIAL_PROJECT_TYPE),
                               JsonCollections.<Provider<? extends AbstractTemplatePage>>createArray(
                                       createNewProjectWizardTutorialPageProvider));
        templateAgent.register(NEW_RESOURCE_WIZARD_TUTORIAL_ID,
                               "Tutorial project that illustrates examples of using New resource wizard.",
                               resources.codenvyTutorialTemplate(),
                               PRIMARY_NATURE,
                               JsonCollections.createArray(TUTORIAL_PROJECT_TYPE),
                               JsonCollections.<Provider<? extends AbstractTemplatePage>>createArray(
                                       createNewResourceWizardTutorialPageProvider));
        templateAgent.register(PARTS_TUTORIAL_ID,
                               "Tutorial project that illustrates examples of using Part API.",
                               resources.codenvyTutorialTemplate(),
                               PRIMARY_NATURE,
                               JsonCollections.createArray(TUTORIAL_PROJECT_TYPE),
                               JsonCollections.<Provider<? extends AbstractTemplatePage>>createArray(createPartsTutorialPageProvider));
    }
}
