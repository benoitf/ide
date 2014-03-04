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
package com.codenvy.ide.project.properties;

import elemental.html.TableElement;

import com.codenvy.ide.Resources;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.resources.model.Property;
import com.codenvy.ide.util.dom.Elements;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * UI for project's properties listing and editing.
 * 
 * @author Ann Shumilova
 */
public class ProjectPropertiesViewImpl extends DialogBox implements ProjectPropertiesView {
    interface ProjectPropertiesViewImplUiBinder extends UiBinder<Widget, ProjectPropertiesViewImpl> {
    }

    @UiField
    Button                                        btnCancel;
    @UiField
    Button                                        btnSave;
    @UiField
    Button                                        btnDelete;
    @UiField
    Button                                        btnEdit;
    @UiField
    Button                                        btnAdd;
    @UiField(provided = true)
    CellTable<Property>                           propertiesTable;

    @UiField(provided = true)
    Resources                                     res;
    private ActionDelegate                        delegate;
    @UiField(provided = true)
    ProjectPropertiesLocalizationConstant locale;


    @Inject
    protected ProjectPropertiesViewImpl(Resources resources,
                                        ProjectPropertiesLocalizationConstant locale,
                                        ProjectPropertiesViewImplUiBinder uiBinder) {
        this.res = resources;
        this.locale = locale;
        initPropertiesTable(resources);

        Widget widget = uiBinder.createAndBindUi(this);

        TableElement tableElement = Elements.createTableElement();
        tableElement.setAttribute("style", "width: 100%");

        this.setText(locale.projectPropertiesViewTitle());
        this.setWidget(widget);

    }

    /**
     * Initialize the properties table.
     * @param resources
     */
    private void initPropertiesTable(Resources resources) {
        propertiesTable = new CellTable<Property>(15, resources);
        Column<Property, String> nameColumn = new Column<Property, String>(new TextCell()) {
            @Override
            public String getValue(Property object) {
                return PropertyUtil.getHumanReadableName(object.getName());
            }
        };
        nameColumn.setSortable(true);

        Column<Property, SafeHtml> valueColumn = new Column<Property, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(Property object) {
                SafeHtmlBuilder builder = new SafeHtmlBuilder();
                for (String v : object.getValue().asIterable()) {
                    builder.appendEscaped(v).appendHtmlConstant("</br>");
                }
                return builder.toSafeHtml();
            }
        };

        propertiesTable.addColumn(nameColumn, locale.propertyNameTitle());
        propertiesTable.addColumn(valueColumn, locale.propertyValueTitle());
        propertiesTable.setColumnWidth(nameColumn, 40, Style.Unit.PCT);
        propertiesTable.setColumnWidth(valueColumn, 60, Style.Unit.PCT);

        final SingleSelectionModel<Property> selectionModel = new SingleSelectionModel<Property>();
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Property selectedObject = selectionModel.getSelectedObject();
                delegate.selectedProperty(selectedObject);
            }
        });
        propertiesTable.setSelectionModel(selectionModel);
    }


    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public void setEditButtonEnabled(boolean isEnabled) {
        btnEdit.setEnabled(isEnabled);
    }

    /** {@inheritDoc} */
    @Override
    public void setDeleteButtonEnabled(boolean isEnabled) {
        btnDelete.setEnabled(isEnabled);
    }

    /** {@inheritDoc} */
    @Override
    public void setSaveButtonEnabled(boolean isEnabled) {
        btnSave.setEnabled(isEnabled);
    }

    /** {@inheritDoc} */
    @Override
    public void setProperties(Array<Property> properties) {
        // Wraps Array in java.util.List
        List<Property> list = new ArrayList<Property>();
        for (Property property : properties.asIterable()) {
            if (property.getValue() != null) {
                list.add(property);
            }
        }
        this.propertiesTable.setRowData(list);
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
    }

    @UiHandler("btnCancel")
    void onBtnCancelClick(ClickEvent event) {
        delegate.onCancelClicked();
    }

    @UiHandler("btnSave")
    void onBtnOpenClick(ClickEvent event) {
        delegate.onSaveClicked();
    }

    @UiHandler("btnEdit")
    void onBtnEditClick(ClickEvent event) {
        delegate.onEditClicked();
    }

    @UiHandler("btnAdd")
    void onBtnAddClick(ClickEvent event) {
        delegate.onAddClicked();
    }

    @UiHandler("btnDelete")
    void onBtnDeleteClick(ClickEvent event) {
        delegate.onDeleteClicked();
    }
}
