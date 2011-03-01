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
package org.exoplatform.ide.client.editor.custom;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.layout.HLayout;

import org.exoplatform.gwtframework.ui.client.component.CheckboxItem;
import org.exoplatform.gwtframework.ui.client.component.DynamicForm;
import org.exoplatform.gwtframework.ui.client.component.IButton;
import org.exoplatform.ide.client.Images;
import org.exoplatform.ide.client.framework.settings.ApplicationSettings;
import org.exoplatform.ide.client.framework.ui.DialogWindow;
import org.exoplatform.ide.client.framework.vfs.File;

import java.util.Map;

public class OpenFileWithForm extends DialogWindow implements OpenFileWithPresenter.Display
{
   private static final int WIDTH = 400;

   private static final int HEIGHT = 250;

   private static final String ID = "ideallOpenFileWithForm";
   
   private static final String EDITORS_LISTGRID_ID = "ideOpenFileWithListGrid";
   
   private static final String OPEN_BUTTON_ID = "ideOpenFileWithOkButton";
   
   private static final String CANCEL_BUTTON_ID = "ideOpenFileWithCancelButton";

   private static final String TITLE = "Open File With";

   private EditorsListGrid editorsListGrid;

   private CheckboxItem useAsDef;

   private IButton openButton;

   private IButton cancelButton;

   private OpenFileWithPresenter presenter;

   public OpenFileWithForm(HandlerManager eventBus, File selectedFile, Map<String, File> openedFiles,
      ApplicationSettings applicationSettings)
   {

      super(eventBus, WIDTH, HEIGHT, ID);
      setTitle(TITLE);

      createEditorListGrid();
      createChecBoxField();

      createButtonsForm();

      show();

      presenter = new OpenFileWithPresenter(eventBus, selectedFile, openedFiles, applicationSettings);
      presenter.bindDisplay(this);

      addCloseClickHandler(new CloseClickHandler()
      {

         public void onCloseClick(CloseClientEvent event)
         {
            destroy();
         }
      });

   }

   private void createEditorListGrid()
   {
      editorsListGrid = new EditorsListGrid();
      editorsListGrid.setID(EDITORS_LISTGRID_ID);
      editorsListGrid.setHeight(135);
      editorsListGrid.setMargin(10);
      addItem(editorsListGrid);
   }

   private void createChecBoxField()
   {
      DynamicForm form = new DynamicForm();
      form.setHeight(30+"px");
      form.setMargin(5);
      useAsDef = new CheckboxItem("Default", "Use as default editor");
      form.add(useAsDef);
      addItem(form);
   }

   private void createButtonsForm()
   {
      HLayout buttonsLayout = new HLayout();
      buttonsLayout.setAutoWidth();
      buttonsLayout.setHeight(22);
      buttonsLayout.setLayoutAlign(Alignment.CENTER);
      buttonsLayout.setMembersMargin(5);

      openButton = new IButton("Open");
      openButton.setID(OPEN_BUTTON_ID);
      openButton.setWidth(90);
      openButton.setHeight(22);
      openButton.setIcon(Images.Buttons.OK);
      openButton.disable();

      cancelButton = new IButton("Cancel");
      cancelButton.setID(CANCEL_BUTTON_ID);
      cancelButton.setWidth(90);
      cancelButton.setHeight(22);
      cancelButton.setIcon(Images.Buttons.CANCEL);

      buttonsLayout.addMember(openButton);
      buttonsLayout.addMember(cancelButton);
      addItem(buttonsLayout);
   }

   public void closeForm()
   {
      destroy();
   }

   @Override
   public void destroy()
   {
      presenter.destroy();
      super.destroy();
   }

   public EditorsListGrid getEditorsListGrid()
   {
      return editorsListGrid;
   }

   public HasValue<Boolean> getIsDefaultCheckItem()
   {
      return useAsDef;
   }

   public HasClickHandlers getCancelButton()
   {
      return cancelButton;
   }

   public HasClickHandlers getOkButton()
   {
      return openButton;
   }

   public void enableOpenButton()
   {
      openButton.enable();
   }
   
}
