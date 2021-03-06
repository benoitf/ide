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
package com.codenvy.ide.api.ui.wizard.newproject;

import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.api.project.shared.dto.ProjectTemplateDescriptor;
import com.codenvy.api.project.shared.dto.ProjectTypeDescriptor;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.paas.PaaS;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardContext;
import com.codenvy.ide.api.ui.wizard.WizardPage;
import com.codenvy.ide.api.ui.wizard.paas.AbstractPaasPage;
import com.codenvy.ide.collections.Array;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import javax.validation.constraints.NotNull;

/**
 * The wizard for creating new project. A user pre-defines behavior of
 * the wizard already from the start page by choosing a technology and
 * PaaS. Depending on the PaaS choice made, the wizard shows different
 * pages after 'choose a template' page.
 *
 * @author Andrey Plotnikov
 */
@Singleton
public class NewProjectWizard extends DefaultWizard {
    public static final WizardContext.Key<PaaS>                      PAAS         = new WizardContext.Key<PaaS>("PaaS");
    public static final WizardContext.Key<ProjectTemplateDescriptor> TEMPLATE     = new WizardContext.Key<ProjectTemplateDescriptor>("Template");
    public static final WizardContext.Key<String>                    PROJECT_NAME = new WizardContext.Key<String>("Project name");
    public static final WizardContext.Key<ProjectTypeDescriptor>     PROJECT_TYPE =
            new WizardContext.Key<ProjectTypeDescriptor>("Project type");
    public static final WizardContext.Key<ProjectDescriptor>         PROJECT      = new WizardContext.Key<ProjectDescriptor>("Project");
    private int chooseTemplate;
    private int lastTemplatePage;

    /**
     * Create new project wizard.
     *
     * @param notificationManager
     */
    @Inject
    public NewProjectWizard(NotificationManager notificationManager) {
        super(notificationManager, "New project");
        chooseTemplate = 1;
        lastTemplatePage = 2;
    }

    /**
     * Add a page after the first page in the wizard, namely between the start page and template choosing page.
     *
     * @param wizardPage
     *         page that needs to be added
     */
    public void addPageAfterFirst(@NotNull Provider<? extends WizardPage> wizardPage) {
        addPage(wizardPage, 1, false);
    }

    /**
     * Add a page after 'choose template' page.
     *
     * @param wizardPage
     *         page that needs to be added
     */
    public void addPageAfterChooseTemplate(@NotNull Provider<? extends WizardPage> wizardPage) {
        addPage(wizardPage, chooseTemplate + 1, false);
    }

    /**
     * Add a page before paas pages.
     *
     * @param wizardPage
     *         page that needs to be added
     */
    public void addPageBeforePaas(@NotNull Provider<? extends WizardPage> wizardPage) {
        addPage(wizardPage, lastTemplatePage, false);
    }

    /**
     * Add a PaaS page.
     *
     * @param paasPage
     *         page that needs to be added
     */
    public void addPaaSPage(@NotNull Provider<? extends AbstractPaasPage> paasPage) {
        addPage(paasPage, lastTemplatePage + 1, false);
    }

    /** {@inheritDoc} */
    @Override
    public void addPage(@NotNull Provider<? extends WizardPage> page, int index, boolean replace) {
        super.addPage(page, index, replace);

        if (index <= chooseTemplate) {
            chooseTemplate++;
        }

        if (index <= lastTemplatePage) {
            lastTemplatePage++;
        }
    }

    /**
     * @return all pages in this wizard.
     *         <br />
     *         IMPORTANT !!! call this method only after <code>flipToFirst()</code>
     */
    public Array<WizardPage> getPages() {
        return wizardPages;
    }
}