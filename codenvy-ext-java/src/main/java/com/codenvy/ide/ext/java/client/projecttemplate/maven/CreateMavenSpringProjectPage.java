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
package com.codenvy.ide.ext.java.client.projecttemplate.maven;

import com.codenvy.api.project.shared.dto.ProjectTypeDescriptor;
import com.codenvy.ide.api.resources.ManageProjectsClientService;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.wizard.template.AbstractTemplatePage;
import com.codenvy.ide.ext.java.client.JavaExtension;
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
import static com.codenvy.ide.ext.java.client.JavaExtension.SPRING_PROJECT_TYPE_ID;
import static com.codenvy.ide.ext.java.client.projectmodel.JavaProjectDescription.ATTRIBUTE_SOURCE_FOLDERS;

/**
 * The wizard page for creating a Spring project from a template.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
@Singleton
public class CreateMavenSpringProjectPage extends AbstractTemplatePage {
    private ManageProjectsClientService   manageProjectsClientService;
    private ProjectTypeDescriptorRegistry projectTypeDescriptorRegistry;
    private UnzipTemplateClientService    unzipTemplateClientService;
    private ResourceProvider              resourceProvider;

    /**
     * Create page.
     *
     * @param service
     *         service that makes it possible to create this kind of project
     * @param resourceProvider
     */
    @Inject
    public CreateMavenSpringProjectPage(ManageProjectsClientService manageProjectsClientService,
                                        ProjectTypeDescriptorRegistry projectTypeDescriptorRegistry,
                                        UnzipTemplateClientService unzipTemplateClientService, ResourceProvider resourceProvider) {
        super(null, null, JavaExtension.MAVEN_SPRING_TEMPLATE_ID);
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
        sourceFolders.add("src/main/java");
        attributes.put(ATTRIBUTE_SOURCE_FOLDERS, sourceFolders);

        final String projectName = wizardContext.getData(PROJECT_NAME);
        ProjectTypeDescriptor springDescriptor = projectTypeDescriptorRegistry.getDescriptor(SPRING_PROJECT_TYPE_ID);
        try {
            manageProjectsClientService.createProject(projectName, springDescriptor, attributes, new AsyncRequestCallback<Void>() {
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
            unzipTemplateClientService.unzipMavenSpringTemplate(projectName, new AsyncRequestCallback<Void>() {
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