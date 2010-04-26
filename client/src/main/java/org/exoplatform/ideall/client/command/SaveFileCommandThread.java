/**
 * Copyright (C) 2009 eXo Platform SAS.
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
 *
 */
package org.exoplatform.ideall.client.command;

import org.exoplatform.gwtframework.commons.component.Handlers;
import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.exception.ExceptionThrownHandler;
import org.exoplatform.ideall.client.event.file.FileSavedEvent;
import org.exoplatform.ideall.client.event.file.SaveFileEvent;
import org.exoplatform.ideall.client.event.file.SaveFileHandler;
import org.exoplatform.ideall.client.model.ApplicationContext;
import org.exoplatform.ideall.client.model.vfs.api.File;
import org.exoplatform.ideall.client.model.vfs.api.VirtualFileSystem;
import org.exoplatform.ideall.client.model.vfs.api.event.FileContentSavedEvent;
import org.exoplatform.ideall.client.model.vfs.api.event.FileContentSavedHandler;
import org.exoplatform.ideall.client.model.vfs.api.event.ItemPropertiesSavedEvent;
import org.exoplatform.ideall.client.model.vfs.api.event.ItemPropertiesSavedHandler;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class SaveFileCommandThread implements FileContentSavedHandler, ItemPropertiesSavedHandler,
   ExceptionThrownHandler, SaveFileHandler
{

   private ApplicationContext context;

   private Handlers handlers;
   
   private HandlerManager eventBus;

   public SaveFileCommandThread(HandlerManager eventBus, ApplicationContext context)
   {
      this.context = context;
      this.eventBus = eventBus;
      handlers = new Handlers(eventBus);
      eventBus.addHandler(SaveFileEvent.TYPE, this);
   }

   public void onSaveFile(SaveFileEvent event)
   {
      handlers.addHandler(FileContentSavedEvent.TYPE, this);
      handlers.addHandler(ItemPropertiesSavedEvent.TYPE, this);
      handlers.addHandler(ExceptionThrownEvent.TYPE, this);

      File file = event.getFile() != null ? event.getFile() : context.getActiveFile();

      if (file.isContentChanged())
      {
         VirtualFileSystem.getInstance().saveContent(file);
         return;
      }
      else
      {
         if (file.isPropertiesChanged())
         {
            VirtualFileSystem.getInstance().saveProperties(file);
            return;
         }
      }

      handlers.removeHandlers();
   }

   public void onFileContentSaved(FileContentSavedEvent event)
   {
      if (event.getFile().isPropertiesChanged())
      {
         VirtualFileSystem.getInstance().saveProperties(event.getFile());
      }
      else
      {
         eventBus.fireEvent(new FileSavedEvent(event.getFile(), null));
         VirtualFileSystem.getInstance().getProperties(event.getFile());
         handlers.removeHandlers();
      }
   }

   public void onItemPropertiesSaved(ItemPropertiesSavedEvent event)
   {
      handlers.removeHandlers();
      eventBus.fireEvent(new FileSavedEvent((File)event.getItem(), null));
      VirtualFileSystem.getInstance().getProperties(event.getItem());
   }

   public void onError(ExceptionThrownEvent event)
   {
      handlers.removeHandlers();
   }

}
