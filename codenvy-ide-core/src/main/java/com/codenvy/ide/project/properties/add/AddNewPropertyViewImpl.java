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
package com.codenvy.ide.project.properties.add;

import com.codenvy.ide.project.properties.ProjectPropertiesLocalizationConstant;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * UI for adding new property.
 *
 * @author Ann Shumilova
 */
@Singleton
public class AddNewPropertyViewImpl extends DialogBox implements AddNewPropertyView {
    interface AddNewPropertyViewImplUiBinder extends UiBinder<Widget, AddNewPropertyViewImpl> {
    }

    @UiField
    TextBox                               name;
    @UiField
    TextBox                               value;
    @UiField
    Button                                btnOk;
    @UiField
    Button                                btnCancel;
    @UiField(provided = true)
    ProjectPropertiesLocalizationConstant locale;

    private ActionDelegate delegate;

    /**
     * Create view.
     *
     * @param locale
     * @param uiBinder
     */
    @Inject
    protected AddNewPropertyViewImpl(ProjectPropertiesLocalizationConstant locale, AddNewPropertyViewImplUiBinder uiBinder) {
        this.locale = locale;
        Widget widget = uiBinder.createAndBindUi(this);
        this.setText(locale.addPropertyViewTitle());
        this.setWidget(widget);
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        this.hide();
    }

    /** {@inheritDoc} */
    @Override
    public void showDialog() {
        this.center();
        this.show();
        name.setFocus(true);
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @UiHandler("btnOk")
    public void onOkClicked(ClickEvent event) {
        delegate.onOkClicked();
    }

    @UiHandler("btnCancel")
    public void onCancelClicked(ClickEvent event) {
        delegate.onCancelClicked();
    }

    @UiHandler({"value"})
    public void onValueChanged(KeyUpEvent event) {
        delegate.onValueChanged();
    }

    @UiHandler({"name"})
    public void onNameChanged(KeyUpEvent event) {
        delegate.onNameChanged();
    }

    /** {@inheritDoc} */
    @Override
    public void setOkButtonEnabled(boolean isEnabled) {
        btnOk.setEnabled(isEnabled);
    }

    /** {@inheritDoc} */
    @Override
    public String getPropertyValue() {
        return this.value.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public String getPropertyName() {
        return name.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public void clearNameField() {
        name.setValue("");
    }

    /** {@inheritDoc} */
    @Override
    public void clearValueField() {
        value.setValue("");
    }
}
