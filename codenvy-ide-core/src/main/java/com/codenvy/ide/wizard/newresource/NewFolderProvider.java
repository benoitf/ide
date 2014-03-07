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
package com.codenvy.ide.wizard.newresource;

import com.codenvy.ide.Resources;
import com.codenvy.ide.api.selection.Selection;
import com.codenvy.ide.api.selection.SelectionAgent;
import com.codenvy.ide.api.ui.IconRegistry;
import com.codenvy.ide.api.ui.wizard.newresource.NewResourceProvider;
import com.codenvy.ide.resources.model.Folder;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.resources.model.Resource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import javax.validation.constraints.NotNull;

import static com.codenvy.ide.resources.model.Folder.TYPE;


/**
 * Provides creating of a new folder.
 *
 * @author <a href="mailto:aplotnikov@exoplatform.com">Andrey Plotnikov</a>
 */
public class NewFolderProvider extends NewResourceProvider {
    private SelectionAgent selectionAgent;
    private IconRegistry iconRegistry;

    @Inject
    public NewFolderProvider(Resources resources, SelectionAgent selectionAgent, IconRegistry iconRegistry) {
        super("Folder", "Folder", iconRegistry.getIcon("default.folder.small.icon"), null);
        this.selectionAgent = selectionAgent;
        this.iconRegistry = iconRegistry;
    }

    /** {@inheritDoc} */
    @Override
    public void create(@NotNull String name, @NotNull Folder parent, @NotNull Project project,
                       @NotNull final AsyncCallback<Resource> callback) {
        project.createFolder(parent, name, new AsyncCallback<Folder>() {
            @Override
            public void onSuccess(Folder result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public boolean inContext() {
        Selection<?> selection = selectionAgent.getSelection();
        if (selection != null) {
            if (selectionAgent.getSelection().getFirstElement() instanceof Resource) {
                Resource resource = (Resource)selectionAgent.getSelection().getFirstElement();
                if (resource.isFile()) {
                    resource = resource.getParent();
                }
                return resource instanceof Project || resource instanceof Folder && resource.getResourceType().equals(TYPE);
            }
        }
        return false;
    }
}