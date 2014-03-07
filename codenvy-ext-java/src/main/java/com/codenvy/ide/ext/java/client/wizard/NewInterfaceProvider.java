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
package com.codenvy.ide.ext.java.client.wizard;

import com.codenvy.ide.api.selection.SelectionAgent;
import com.codenvy.ide.api.ui.IconRegistry;
import com.codenvy.ide.ext.java.client.JavaResources;
import com.codenvy.ide.ext.java.client.projectmodel.JavaProject;
import com.codenvy.ide.resources.model.Folder;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.resources.model.Resource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import javax.validation.constraints.NotNull;

/**
 * Provides creating of a java interface.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
public class NewInterfaceProvider extends AbstractNewJavaResourceProvider {

    private IconRegistry iconRegistry;

    @Inject
    public NewInterfaceProvider(SelectionAgent selectionAgent, IconRegistry iconRegistry) {
        super("Java Interface", "Java Interface", iconRegistry.getIcon("java.class"), "java", selectionAgent);
        this.iconRegistry = iconRegistry;
    }

    /** {@inheritDoc} */
    @Override
    public void create(@NotNull String name, @NotNull Folder parent, @NotNull Project project,
                       @NotNull final AsyncCallback<Resource> callback) {
        StringBuilder content = new StringBuilder(getPackage(parent, ((JavaProject)project).getSourceFolders()));
        content.append("public interface ").append(name).append(TYPE_CONTENT);

        createFile(name, parent, project, callback, content.toString());
    }
}