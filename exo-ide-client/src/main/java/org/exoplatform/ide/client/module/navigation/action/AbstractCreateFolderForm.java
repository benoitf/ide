/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.ide.client.module.navigation.action;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.StatefulCanvas;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.ToolbarItem;
import com.smartgwt.client.widgets.layout.VLayout;

import org.exoplatform.gwtframework.ui.client.smartgwt.component.IButton;
import org.exoplatform.gwtframework.ui.client.smartgwt.component.TextField;
import org.exoplatform.ide.client.Images;
import org.exoplatform.ide.client.framework.ui.DialogWindow;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version @version $Id: $
 */

public abstract class AbstractCreateFolderForm extends DialogWindow implements CreateFolderDisplay
{

   public static final int WIDTH = 400;

   public static final int HEIGHT = 160;

   public static final String ID = "ideCreateFolderForm";
   
   public final String ID_CREATE_BUTTON = "ideCreateFolderFormCreateButton";
   
   public final String ID_CANCEL_BUTTON = "ideCreateFolderFormCancelButton";
   
   public final String ID_DYNAMIC_FORM = "ideCreateFolderFormDynamicForm";
   
   public final String NAME_FIELD = "ideCreateFolderFormNameField";

   private VLayout vLayout;

   private TextField folderNameField;

   private IButton createButton;

   private IButton cancelButton;
   
   private String submitButtonTitle;

   public AbstractCreateFolderForm(HandlerManager eventBus, String title, String submitButtonTitle)
   {
      super(eventBus, WIDTH, HEIGHT, ID);
      setTitle(title);
      
      this.submitButtonTitle = submitButtonTitle;

      vLayout = new VLayout();
      addItem(vLayout);

      createFieldForm();
      createButtons();

      show();

      addCloseClickHandler(new CloseClickHandler()
      {
         public void onCloseClick(CloseClientEvent event)
         {
            destroy();
         }
      });
      
   }

   private void createFieldForm()
   {
      DynamicForm paramsForm = new DynamicForm();
      paramsForm.setID(ID_DYNAMIC_FORM);
      paramsForm.setPadding(5);
      paramsForm.setWidth(300);
      paramsForm.setLayoutAlign(Alignment.CENTER);
      paramsForm.setPadding(15);
      paramsForm.setAutoFocus(true);

      StaticTextItem caption = new StaticTextItem();
      caption.setDefaultValue("Name of new folder:");
      caption.setShowTitle(false);
      caption.setColSpan(2);

      SpacerItem delimiter = new SpacerItem();
      delimiter.setColSpan(2);
      delimiter.setHeight(5);

      folderNameField = new TextField();
      folderNameField.setName(NAME_FIELD);
      folderNameField.setShowTitle(false);
      folderNameField.setWidth(300);

      paramsForm.setFields(caption, delimiter, folderNameField);
      paramsForm.focusInItem(folderNameField);

      vLayout.addMember(paramsForm);
   }

   private void createButtons()
   {
      DynamicForm buttonsForm = new DynamicForm();
      buttonsForm.setPadding(5);
      buttonsForm.setHeight(24);
      buttonsForm.setLayoutAlign(Alignment.CENTER);

      createButton = new IButton(submitButtonTitle);
      createButton.setID(ID_CREATE_BUTTON);
      createButton.setWidth(90);
      createButton.setHeight(22);
      createButton.setIcon(Images.Buttons.OK);

      cancelButton = new IButton("Cancel");
      cancelButton.setID(ID_CANCEL_BUTTON);
      cancelButton.setWidth(90);
      cancelButton.setHeight(22);
      cancelButton.setIcon(Images.Buttons.NO);

      ToolbarItem tbi = new ToolbarItem();
      StatefulCanvas delimiter1 = new StatefulCanvas();
      delimiter1.setWidth(3);
      tbi.setButtons(createButton, delimiter1, cancelButton);
      buttonsForm.setFields(tbi);

      buttonsForm.setAutoWidth();
      vLayout.addMember(buttonsForm);
   }

   public void closeForm()
   {
      destroy();
   }

   public HasClickHandlers getCancelButton()
   {
      return cancelButton;
   }

   public HasClickHandlers getCreateButton()
   {
      return createButton;
   }

   public HasValue<String> getFolderNameField()
   {
      return folderNameField;
   }

   public HasKeyPressHandlers getFolderNameFiledKeyPressed()
   {
      return (HasKeyPressHandlers)folderNameField;
   }

}
