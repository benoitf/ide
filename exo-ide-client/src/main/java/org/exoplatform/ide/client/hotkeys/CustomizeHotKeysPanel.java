/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.ide.client.hotkeys;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.StatefulCanvas;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.layout.VLayout;

import org.exoplatform.gwtframework.ui.client.command.Control;
import org.exoplatform.gwtframework.ui.client.component.DynamicForm;
import org.exoplatform.gwtframework.ui.client.component.IButton;
import org.exoplatform.gwtframework.ui.client.component.TextField;
import org.exoplatform.ide.client.Images;
import org.exoplatform.ide.client.framework.settings.ApplicationSettings;
import org.exoplatform.ide.client.framework.ui.DialogWindow;

import java.util.List;

/**
 * Created by The eXo Platform SAS.
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id:
 *
 */
public class CustomizeHotKeysPanel extends DialogWindow implements CustomizeHotKeysPresenter.Display
{

   public static interface Style
   {

      static final String ERROR = "exo-cutomizeHotKey-label-error";

   }

   private static final int BUTTON_FORM_HEIGHT = 25;

   private static final int WIDTH = 600;

   private static final int HEIGHT = 340;

   private static final String ID = "ideCustomizeHotKeysForm";

   private static final String LIST_GRID_ID = "ideCustomizeHotKeysFormListGrid";

   private static final String DYNAMIC_FORM_HOTKEY_FIELD_ID = "ideCustomizeHotKeysFormDynamicFormHotKeyField";

   private static final String HOTKEY_FIELD_NAME = "ideCustomizeHotKeysFormHotKeyField";

   private static final String BIND_BUTTON_ID = "ideCustomizeHotKeysFormBindButton";

   private static final String UNBIND_BUTTON_ID = "ideCustomizeHotKeysFormUnbindButton";

   private static final String SAVE_BUTTON_ID = "ideCustomizeHotKeysFormSaveButton";

   private static final String CANCEL_BUTTON_ID = "ideCustomizeHotKeysFormCancelButton";

   private static final String TITLE = "Customize hotkeys";

   private static final int TEXT_FIELD_WIDTH = 90;

   private static final int TEXT_FIELD_HEIGHT = 22;

   private static final int BUTTON_WIDTH = 90;

   private static final int BUTTON_HEIGHT = 22;

   private static final int FORM_DELIMITER_HEIGHT = 5;

   private HorizontalPanel hLayout;

   private VLayout vLayout;

   private IButton saveButton;

   private IButton cancelButton;

   private CustomizeHotKeysPresenter presenter;

   private HotKeyItemListGrid hotKeyItemListGrid;

   private TextField hotKeyField;

   private IButton bindButton;

   private IButton unbindButton;

   private Label errorLabel;

   public CustomizeHotKeysPanel(HandlerManager eventBus, ApplicationSettings applicationSettings, List<Control> controls)
   {
      super(eventBus, WIDTH, HEIGHT, ID);

      setTitle(TITLE);
      setShowMaximizeButton(true);

      vLayout = new VLayout();
      vLayout.setMargin(10);
      vLayout.setWidth100();
      vLayout.setAlign(VerticalAlignment.TOP);

      hLayout = new HorizontalPanel();
      hLayout.setHeight((BUTTON_HEIGHT + 4)+"px");
      hLayout.setWidth("100%");
      createHotKeysListGrid();

      Canvas delimiter = new StatefulCanvas();
      delimiter.setHeight(FORM_DELIMITER_HEIGHT);

      vLayout.addMember(delimiter);

      hLayout.add(createHotKeyForm());
      hLayout.add(createBindButtonsForm());
      HorizontalPanel buttonsPanel = createButtonsForm();
      hLayout.add(buttonsPanel);

      vLayout.addMember(hLayout);

      createErrorLabel();

      addItem(vLayout);

      try
      {
         show();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      presenter = new CustomizeHotKeysPresenter(eventBus, applicationSettings, controls);
      presenter.bindDisplay(this);

      addCloseClickHandler(new CloseClickHandler()
      {

         public void onCloseClick(CloseClientEvent event)
         {
            destroy();
         }
      });
   }

   @Override
   protected void onDestroy()
   {
      presenter.destroy();
      super.onDestroy();
   }

   public HotKeyItemListGrid getHotKeyItemListGrid()
   {
      return hotKeyItemListGrid;
   }

   private void createHotKeysListGrid()
   {
      hotKeyItemListGrid = new HotKeyItemListGrid();
      hotKeyItemListGrid.setID(LIST_GRID_ID);
      vLayout.addMember(hotKeyItemListGrid);
   }

   private DynamicForm createHotKeyForm()
   {
      DynamicForm hotKeyform = new DynamicForm();
      hotKeyform.setID(DYNAMIC_FORM_HOTKEY_FIELD_ID);
      hotKeyform.setPadding(5);
      hotKeyform.setHeight(BUTTON_FORM_HEIGHT);

      hotKeyField = new TextField();
      hotKeyField.setName(HOTKEY_FIELD_NAME);
      hotKeyField.setWidth(TEXT_FIELD_WIDTH);
      hotKeyField.setHeight(TEXT_FIELD_HEIGHT);
      hotKeyField.setShowTitle(false);

      hotKeyform.add(hotKeyField);

      return hotKeyform;
   }

   private HorizontalPanel createBindButtonsForm()
   {
      HorizontalPanel buttonsLayout = new HorizontalPanel();
      buttonsLayout.setHeight(BUTTON_HEIGHT + "px");
      buttonsLayout.setSpacing(5);

      bindButton = new IButton("Bind");
      bindButton.setID(BIND_BUTTON_ID);
      bindButton.setWidth(BUTTON_WIDTH);
      bindButton.setHeight(BUTTON_HEIGHT);
      bindButton.setIcon(Images.Buttons.YES);
      bindButton.setDisabled(true);

      unbindButton = new IButton("Unbind");
      unbindButton.setID(UNBIND_BUTTON_ID);
      unbindButton.setWidth(BUTTON_WIDTH);
      unbindButton.setHeight(BUTTON_HEIGHT);
      unbindButton.setIcon(Images.Buttons.YES);
      unbindButton.setDisabled(true);

      buttonsLayout.add(bindButton);
      buttonsLayout.add(unbindButton);
      return buttonsLayout;
   }

   private HorizontalPanel createButtonsForm()
   {
      HorizontalPanel buttonsLayout = new HorizontalPanel();
      buttonsLayout.setHeight(BUTTON_HEIGHT + "px");
      buttonsLayout.setSpacing(5);

      saveButton = new IButton("Save");
      saveButton.setID(SAVE_BUTTON_ID);
      saveButton.setWidth(BUTTON_WIDTH);
      saveButton.setHeight(BUTTON_HEIGHT);
      saveButton.setIcon(Images.Buttons.YES);
      saveButton.setDisabled(true);

      cancelButton = new IButton("Cancel");
      cancelButton.setID(CANCEL_BUTTON_ID);
      cancelButton.setWidth(BUTTON_WIDTH);
      cancelButton.setHeight(BUTTON_HEIGHT);
      cancelButton.setIcon(Images.Buttons.NO);

      buttonsLayout.add(saveButton);
      buttonsLayout.add(cancelButton);

      return buttonsLayout;
   }

   private void createErrorLabel()
   {
      errorLabel = new Label();
      errorLabel.setHeight("16px");
      errorLabel.setText("");
      errorLabel.setStyleName(Style.ERROR);
      vLayout.addMember(errorLabel);
   }

   public void showError(String style, String text)
   {
      if (text == null)
      {
         errorLabel.setText("");
      }
      else
      {
         errorLabel.setText(text);
         errorLabel.setStyleName(style);
      }
   }

   public HasClickHandlers getCancelButton()
   {
      return cancelButton;
   }

   public HasClickHandlers getSaveButton()
   {
      return saveButton;
   }

   public void closeForm()
   {
      destroy();
   }

   public void disableSaveButton(boolean disabled)
   {
      saveButton.setDisabled(disabled);
   }

   public void disableSaveButton()
   {
      saveButton.setDisabled(true);
   }

   public void enableSaveButton()
   {
      saveButton.setDisabled(false);
   }

   public void disableBindButton()
   {
      bindButton.disable();
   }

   public void disableUnbindButton()
   {
      unbindButton.disable();
   }

   public void enableBindButton()
   {
      bindButton.enable();
   }

   public void enableUnbindButton()
   {
      unbindButton.enable();
   }

   public HasClickHandlers getBindButton()
   {
      return bindButton;
   }

   public HasClickHandlers getUnbindButton()
   {
      return unbindButton;
   }

   public HasValue<String> getHotKeyField()
   {
      return hotKeyField;
   }

   public void clearHotKeyField()
   {
      hotKeyField.clearValue();
   }

   public void disableHotKeyField()
   {
      hotKeyField.setDisabled(true);
   }

   public void enableHotKeyField()
   {
      hotKeyField.setDisabled(false);
   }

   public void focusOnHotKeyField()
   {
      hotKeyField.focusInItem();
   }

}
