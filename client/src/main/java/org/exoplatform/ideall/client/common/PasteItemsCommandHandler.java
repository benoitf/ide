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
package org.exoplatform.ideall.client.common;

import java.util.List;

import org.exoplatform.gwtframework.commons.component.Handlers;
import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.exception.ExceptionThrownHandler;
import org.exoplatform.gwtframework.ui.client.dialogs.Dialogs;
import org.exoplatform.ideall.client.browser.event.RefreshBrowserEvent;
import org.exoplatform.ideall.client.event.edit.PasteItemsCompleteEvent;
import org.exoplatform.ideall.client.event.edit.PasteItemsEvent;
import org.exoplatform.ideall.client.event.edit.PasteItemsHandler;
import org.exoplatform.ideall.client.model.ApplicationContext;
import org.exoplatform.ideall.client.model.File;
import org.exoplatform.ideall.client.model.Item;
import org.exoplatform.ideall.client.model.data.DataService;
import org.exoplatform.ideall.client.model.data.event.ItemCopyCompleteEvent;
import org.exoplatform.ideall.client.model.data.event.ItemCopyCompleteHandler;
import org.exoplatform.ideall.client.model.data.event.MoveCompleteEvent;
import org.exoplatform.ideall.client.model.data.event.MoveCompleteHandler;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Created by The eXo Platform SAS.
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
*/
public class PasteItemsCommandHandler implements PasteItemsHandler, ItemCopyCompleteHandler, MoveCompleteHandler,
   ExceptionThrownHandler
{
   private HandlerManager eventBus;

   private ApplicationContext context;

   private Handlers handlers;

   public PasteItemsCommandHandler(HandlerManager eventBus, ApplicationContext context)
   {
      this.eventBus = eventBus;
      this.context = context;
      handlers = new Handlers(eventBus);

      eventBus.addHandler(PasteItemsEvent.TYPE, this);

   }

   private void copyNextItem()
   {
      if (context.getItemsToCopy().size() == 0)
      {
         operationCompleted();
         return;
      }

      Item item = context.getItemsToCopy().get(0);
      System.out.println("copy > " + item.getPath());

      String pathFromCopy = item.getPath();
      pathFromCopy = pathFromCopy.substring(0, pathFromCopy.lastIndexOf("/"));

      if (pathFromCopy.equals(context.getSelectedItems().get(0).getPath()))
      {
         String message = "Can't copy files in the same directory!";
         Dialogs.getInstance().showError(message);
         return;
      }

      String destination = context.getSelectedItems().get(0).getPath() + "/" + item.getName();

      DataService.getInstance().copy(item, destination);
   }

   private void cutNextItem()
   {
      if (context.getItemsToCut().size() == 0)
      {
         operationCompleted();
         return;
      }

      Item item = context.getItemsToCut().get(0);

      String pathFromCut = item.getPath();
      pathFromCut = pathFromCut.substring(0, pathFromCut.lastIndexOf("/"));

      if (pathFromCut.equals(context.getSelectedItems().get(0).getPath()))
      {
         String message = "Can't move files in the same directory!";
         Dialogs.getInstance().showError(message);
         return;
      }

      String destination = context.getSelectedItems().get(0).getPath() + "/" + item.getName();

      DataService.getInstance().move(item, destination);
   }

   private void operationCompleted()
   {
      eventBus.fireEvent(new PasteItemsCompleteEvent());
      // eventBus.fireEvent(new SetFocusOnItemEvent(context.getSelectedItems().get(0).getPath()));
      eventBus.fireEvent(new RefreshBrowserEvent());
      handlers.removeHandlers();
   }

   private boolean isFilesOpen(List<Item> items)
   {
      for (Item i : items)
      {
         if (i instanceof File)
         {
            if (context.getOpenedFiles().containsValue(i))
            {
               return true;
            }
         }
      }
      return false;
   }

   public void onPasteItems(PasteItemsEvent event)
   {
      if (context.getItemsToCopy().size() != 0)
      {
         handlers.addHandler(ItemCopyCompleteEvent.TYPE, this);
         handlers.addHandler(ExceptionThrownEvent.TYPE, this);
         copyNextItem();
      }

      if (context.getItemsToCut().size() != 0)
      {
         if (!isFilesOpen(context.getItemsToCut()))
         {
            handlers.addHandler(MoveCompleteEvent.TYPE, this);
            handlers.addHandler(ExceptionThrownEvent.TYPE, this);
            cutNextItem();
         }
         else
         {
            String message = "You should close open files, before cut";
            Dialogs.getInstance().showInfo(message);
            return;
         }
      }

   }

   public void onItemCopyComplete(ItemCopyCompleteEvent event)
   {
      if (context.getItemsToCopy().size() != 0)
      {
         //remove copied item
         context.getItemsToCopy().remove(event.getCopiedItem());
         copyNextItem();
      }
   }

   public void onMoveComplete(MoveCompleteEvent event)
   {
      System.out.println("move complete - " + context.getItemsToCut().size());
      if (context.getItemsToCut().size() != 0)
      {
         context.getItemsToCut().remove(event.getItem());
         cutNextItem();
      }

   }

   public void onError(ExceptionThrownEvent event)
   {
      handlers.removeHandlers();
   }
}
