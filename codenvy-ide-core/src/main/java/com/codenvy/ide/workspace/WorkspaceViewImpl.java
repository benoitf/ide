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
package com.codenvy.ide.workspace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Implements {@link WorkspaceView}
 *
 * @author <a href="mailto:aplotnikov@exoplatform.com">Andrey Plotnikov</a>
 */
public class WorkspaceViewImpl extends LayoutPanel implements WorkspaceView {
    interface WorkspaceViewUiBinder extends UiBinder<Widget, WorkspaceViewImpl> {
    }

    private static WorkspaceViewUiBinder uiBinder = GWT.create(WorkspaceViewUiBinder.class);

    @UiField
    SimpleLayoutPanel perspectivePanel;
    @UiField
    SimplePanel menuPanel;
    @UiField
    SimplePanel toolbarPanel;
    @UiField
    SimplePanel  statusPanel;
    @UiField
    Button      btnUpdate;
    ActionDelegate delegate;

    /** Create view. */
    @Inject
    protected WorkspaceViewImpl() {
        add(uiBinder.createAndBindUi(this));
        getElement().setId("codenvyIdeWorkspaceViewImpl");
    }
    
    /** {@inheritDoc} */
    @Override
    public AcceptsOneWidget getMenuPanel() {
        return menuPanel;
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public AcceptsOneWidget getPerspectivePanel() {
        return perspectivePanel;
    }

    /** {@inheritDoc} */
    @Override
    public AcceptsOneWidget getToolbarPanel() {
        return toolbarPanel;
    }

    /** {@inheritDoc} */
    @Override
    public AcceptsOneWidget getStatusPanel() { return statusPanel;
    }

    /** {@inheritDoc} */
    @Override
    public void setUpdateButtonVisibility(boolean visible) {
        btnUpdate.setVisible(visible);
    }

    @UiHandler("btnUpdate")
    public void onUpdateClicked(ClickEvent event) {
        delegate.onUpdateClicked();
    }
}