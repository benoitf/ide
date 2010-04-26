/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.ideall.client.command;

import org.exoplatform.gwtframework.commons.component.Handlers;
import org.exoplatform.gwtframework.commons.dialogs.Dialogs;
import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.exception.ExceptionThrownHandler;
import org.exoplatform.gwtframework.editor.api.Editor;
import org.exoplatform.gwtframework.editor.api.EditorNotFoundException;
import org.exoplatform.ideall.client.editor.EditorUtil;
import org.exoplatform.ideall.client.editor.event.EditorOpenFileEvent;
import org.exoplatform.ideall.client.event.file.OpenFileEvent;
import org.exoplatform.ideall.client.event.file.OpenFileHandler;
import org.exoplatform.ideall.client.model.ApplicationContext;
import org.exoplatform.ideall.client.model.vfs.api.File;
import org.exoplatform.ideall.client.model.vfs.api.VirtualFileSystem;
import org.exoplatform.ideall.client.model.vfs.api.event.FileContentReceivedEvent;
import org.exoplatform.ideall.client.model.vfs.api.event.FileContentReceivedHandler;
import org.exoplatform.ideall.client.model.vfs.api.event.ItemPropertiesReceivedEvent;
import org.exoplatform.ideall.client.model.vfs.api.event.ItemPropertiesReceivedHandler;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Created by The eXo Platform SAS.
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
*/
public class OpenFileCommandThread implements OpenFileHandler, FileContentReceivedHandler, ExceptionThrownHandler,
   ItemPropertiesReceivedHandler
{
   private HandlerManager eventBus;

   private ApplicationContext context;

   private Handlers handlers;

   public OpenFileCommandThread(HandlerManager eventBus, ApplicationContext context)
   {
      this.eventBus = eventBus;
      this.context = context;

      handlers = new Handlers(eventBus);

      eventBus.addHandler(OpenFileEvent.TYPE, this);
   }

   public void onOpenFile(OpenFileEvent event)
   {
      File file = event.getFile();

      //      if (!IDEMimeTypes.isMimeTypeSupported(file.getContentType()))
      //      {
      //         Dialogs.getInstance().showError("Can't open file <b>" + file.getName() + "</b>!<br>Mime type <b>" + file.getContentType() + "</b> is not supported!");
      //         return;
      //      }

      if (file.getContent() != null)
      {
         open(file);
         return;
      }

      handlers.addHandler(ExceptionThrownEvent.TYPE, this);
      handlers.addHandler(ItemPropertiesReceivedEvent.TYPE, this);
      VirtualFileSystem.getInstance().getProperties(file);
   }

   public void onItemPropertiesReceived(ItemPropertiesReceivedEvent event)
   {
      // TODO Auto-generated method stub
      if (event.getItem() instanceof File)
      {
         handlers.addHandler(FileContentReceivedEvent.TYPE, this);
         VirtualFileSystem.getInstance().getContent((File)event.getItem());
      }

   }

   public void onFileContentReceived(FileContentReceivedEvent event)
   {
      handlers.removeHandlers();
      open(event.getFile());
   }

   private void open(File file)
   {
      try
      {
         Editor editor = EditorUtil.getEditor(file.getContentType(), context);
         eventBus.fireEvent(new EditorOpenFileEvent(file, editor));
      }
      catch (EditorNotFoundException e)
      {
         Dialogs.getInstance().showError("Can't find editor for type <b>" + file.getContentType() + "</b>");
      }
   }

   public void onError(ExceptionThrownEvent event)
   {
      handlers.removeHandlers();
   }

}
