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
package org.exoplatform.ideall.client.module.navigation;

import org.exoplatform.gwtframework.commons.component.Handlers;
import org.exoplatform.ideall.client.action.CreateFolderForm;
import org.exoplatform.ideall.client.action.DeleteItemForm;
import org.exoplatform.ideall.client.action.RenameItemForm;
import org.exoplatform.ideall.client.command.CreateFileCommandThread;
import org.exoplatform.ideall.client.command.GoToFolderCommandThread;
import org.exoplatform.ideall.client.command.OpenFileCommandThread;
import org.exoplatform.ideall.client.command.PasteItemsCommandThread;
import org.exoplatform.ideall.client.command.SaveAllFilesCommandThread;
import org.exoplatform.ideall.client.command.SaveFileAsCommandThread;
import org.exoplatform.ideall.client.command.SaveFileCommandThread;
import org.exoplatform.ideall.client.editor.custom.OpenFileWithForm;
import org.exoplatform.ideall.client.event.edit.CopyItemsEvent;
import org.exoplatform.ideall.client.event.edit.CopyItemsHandler;
import org.exoplatform.ideall.client.event.edit.CutItemsEvent;
import org.exoplatform.ideall.client.event.edit.CutItemsHandler;
import org.exoplatform.ideall.client.event.edit.ItemsToPasteSelectedEvent;
import org.exoplatform.ideall.client.event.file.CreateFolderEvent;
import org.exoplatform.ideall.client.event.file.CreateFolderHandler;
import org.exoplatform.ideall.client.event.file.DeleteItemEvent;
import org.exoplatform.ideall.client.event.file.DeleteItemHandler;
import org.exoplatform.ideall.client.event.file.OpenFileWithEvent;
import org.exoplatform.ideall.client.event.file.OpenFileWithHandler;
import org.exoplatform.ideall.client.event.file.RenameItemEvent;
import org.exoplatform.ideall.client.event.file.RenameItemHander;
import org.exoplatform.ideall.client.event.file.SaveAsTemplateEvent;
import org.exoplatform.ideall.client.event.file.SaveAsTemplateHandler;
import org.exoplatform.ideall.client.event.file.SearchFileEvent;
import org.exoplatform.ideall.client.event.file.SearchFileHandler;
import org.exoplatform.ideall.client.event.file.UploadFileEvent;
import org.exoplatform.ideall.client.event.file.UploadFileHandler;
import org.exoplatform.ideall.client.framework.ui.event.ClearFocusEvent;
import org.exoplatform.ideall.client.model.ApplicationContext;
import org.exoplatform.ideall.client.search.file.SearchForm;
import org.exoplatform.ideall.client.template.SaveAsTemplateForm;
import org.exoplatform.ideall.client.upload.UploadForm;
import org.exoplatform.ideall.vfs.api.File;
import org.exoplatform.ideall.vfs.api.Item;

import com.google.gwt.event.shared.HandlerManager;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
 *
 */
public class NavigationModuleEventHandler implements OpenFileWithHandler, UploadFileHandler, SaveAsTemplateHandler,
   CreateFolderHandler, CopyItemsHandler, CutItemsHandler, RenameItemHander, DeleteItemHandler, SearchFileHandler
{
   private SaveFileCommandThread saveFileCommandHandler;

   private SaveFileAsCommandThread saveFileAsCommandHandler;

   private SaveAllFilesCommandThread saveAllFilesCommandHandler;

   private GoToFolderCommandThread goToFolderCommandHandler;

   private PasteItemsCommandThread pasteItemsCommandHandler;

   private OpenFileCommandThread openFileCommandThread;

   private CreateFileCommandThread createFileCommandThread;

   private HandlerManager eventBus;

   private ApplicationContext context;

   protected Handlers handlers;

   public NavigationModuleEventHandler(HandlerManager eventBus, ApplicationContext context)
   {
      this.eventBus = eventBus;
      this.context = context;

      handlers = new Handlers(eventBus);

      createFileCommandThread = new CreateFileCommandThread(eventBus, context);
      openFileCommandThread = new OpenFileCommandThread(eventBus, context);
      saveFileCommandHandler = new SaveFileCommandThread(eventBus, context);
      saveFileAsCommandHandler = new SaveFileAsCommandThread(eventBus, context);
      saveAllFilesCommandHandler = new SaveAllFilesCommandThread(eventBus, context);
      goToFolderCommandHandler = new GoToFolderCommandThread(eventBus, context);
      pasteItemsCommandHandler = new PasteItemsCommandThread(eventBus, context);

      handlers.addHandler(OpenFileWithEvent.TYPE, this);
      handlers.addHandler(UploadFileEvent.TYPE, this);
      handlers.addHandler(SaveAsTemplateEvent.TYPE, this);
      handlers.addHandler(CreateFolderEvent.TYPE, this);
      handlers.addHandler(CopyItemsEvent.TYPE, this);
      handlers.addHandler(CutItemsEvent.TYPE, this);

      handlers.addHandler(DeleteItemEvent.TYPE, this);
      handlers.addHandler(RenameItemEvent.TYPE, this);
      handlers.addHandler(SearchFileEvent.TYPE, this);

   }

   public void onOpenFileWith(OpenFileWithEvent event)
   {
      new OpenFileWithForm(eventBus, context);
   }

   public void onUploadFile(UploadFileEvent event)
   {
      Item item = context.getSelectedItems(context.getSelectedNavigationPanel()).get(0);

      String path = item.getHref();
      if (item instanceof File)
      {
         path = path.substring(path.lastIndexOf("/"));
      }
      eventBus.fireEvent(new ClearFocusEvent());
      new UploadForm(eventBus, context, path, event.isOpenFile());
   }

   public void onSaveAsTemplate(SaveAsTemplateEvent event)
   {
      File file = context.getActiveFile();
      new SaveAsTemplateForm(eventBus, file);
   }

   public void onCreateFolder(CreateFolderEvent event)
   {
      Item item = context.getSelectedItems(context.getSelectedNavigationPanel()).get(0);

      String href = item.getHref();
      if (item instanceof File)
      {
         href = href.substring(0, href.lastIndexOf("/") + 1);
      }

      new CreateFolderForm(eventBus, context, href);
   }

   public void onCopyItems(CopyItemsEvent event)
   {
      context.getItemsToCopy().clear();
      context.getItemsToCut().clear();
      context.getItemsToCopy().addAll(context.getSelectedItems(context.getSelectedNavigationPanel()));
      eventBus.fireEvent(new ItemsToPasteSelectedEvent());
   }

   public void onCutItems(CutItemsEvent event)
   {
      context.getItemsToCut().clear();
      context.getItemsToCopy().clear();

      context.getItemsToCut().addAll(context.getSelectedItems(context.getSelectedNavigationPanel()));
      eventBus.fireEvent(new ItemsToPasteSelectedEvent());
   }

   public void onRenameItem(RenameItemEvent event)
   {
      new RenameItemForm(eventBus, context);
   }

   public void onDeleteItem(DeleteItemEvent event)
   {
      new DeleteItemForm(eventBus, context);
   }

   public void onSearchFile(SearchFileEvent event)
   {
      new SearchForm(eventBus, context);
   }
}
