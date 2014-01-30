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
package com.codenvy.ide.ext.java.client.projecttemplate.ant;

import com.codenvy.api.project.shared.dto.ProjectTypeDescriptor;
import com.codenvy.ide.api.resources.ManageProjectsClientService;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.wizard.template.AbstractTemplatePage;
import com.codenvy.ide.ext.java.client.projecttemplate.UnzipTemplateClientService;
import com.codenvy.ide.resources.ProjectTypeDescriptorRegistry;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codenvy.ide.api.ui.wizard.newproject.NewProjectWizard.PROJECT;
import static com.codenvy.ide.api.ui.wizard.newproject.NewProjectWizard.PROJECT_NAME;
import static com.codenvy.ide.ext.java.client.JavaExtension.ANT_JAR_TEMPLATE_ID;
import static com.codenvy.ide.ext.java.client.JavaExtension.JAR_PROJECT_TYPE_ID;
import static com.codenvy.ide.ext.java.client.projectmodel.JavaProjectDescription.ATTRIBUTE_SOURCE_FOLDERS;

/**
 * The wizard page for creating a Java project from a template.
 *
 * @author Andrey Plotnikov
 * @author Artem Zatsarynnyy
 */
@Singleton
public class CreateAntJarProjectPage extends AbstractTemplatePage {
    private ManageProjectsClientService   manageProjectsClientService;
    private ProjectTypeDescriptorRegistry projectTypeDescriptorRegistry;
    private UnzipTemplateClientService    unzipTemplateClientService;
    private ResourceProvider              resourceProvider;

    /** Create page. */
    @Inject
    public CreateAntJarProjectPage(ManageProjectsClientService manageProjectsClientService,
                                   ProjectTypeDescriptorRegistry projectTypeDescriptorRegistry,
                                   UnzipTemplateClientService unzipTemplateClientService, ResourceProvider resourceProvider) {
        super(null, null, ANT_JAR_TEMPLATE_ID);
        this.manageProjectsClientService = manageProjectsClientService;
        this.projectTypeDescriptorRegistry = projectTypeDescriptorRegistry;
        this.unzipTemplateClientService = unzipTemplateClientService;
        this.resourceProvider = resourceProvider;
    }

    /** {@inheritDoc} */
    @Override
    public void commit(final CommitCallback callback) {
        Map<String, List<String>> attributes = new HashMap<String, List<String>>(1);
        // TODO: make it as calculated attributes
        List<String> sourceFolders = new ArrayList<String>(1);
        sourceFolders.add("src");
        attributes.put(ATTRIBUTE_SOURCE_FOLDERS, sourceFolders);

        final String projectName = wizardContext.getData(PROJECT_NAME);
        ProjectTypeDescriptor jarDescriptor = projectTypeDescriptorRegistry.getDescriptor(JAR_PROJECT_TYPE_ID);
        try {
            manageProjectsClientService.createProject(projectName, jarDescriptor, attributes, new AsyncRequestCallback<Void>() {
                @Override
                protected void onSuccess(Void result) {
                    unzipTemplate(projectName, callback);
                }

                @Override
                protected void onFailure(Throwable exception) {
                    callback.onFailure(exception);
                }
            });
        } catch (RequestException e) {
            callback.onFailure(e);
        }
    }

    private void unzipTemplate(final String projectName, final CommitCallback callback) {
        try {
            unzipTemplateClientService.unzipAntJarTemplate(projectName, new AsyncRequestCallback<Void>() {
                @Override
                protected void onSuccess(Void result) {
                    resourceProvider.getProject(projectName, new AsyncCallback<Project>() {
                        @Override
                        public void onSuccess(Project result) {
                            wizardContext.putData(PROJECT, result);
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            callback.onFailure(caught);
                        }
                    });
                }

                @Override
                protected void onFailure(Throwable exception) {
                    callback.onFailure(exception);
                }
            });
        } catch (RequestException e) {
            callback.onFailure(e);
        }
    }
}