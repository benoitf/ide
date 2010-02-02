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
package org.exoplatform.ideall.client.download;

import org.exoplatform.gwt.commons.client.Handlers;
import org.exoplatform.ideall.client.application.event.RegisterEventHandlersEvent;
import org.exoplatform.ideall.client.application.event.RegisterEventHandlersHandler;
import org.exoplatform.ideall.client.event.file.DownloadFileEvent;
import org.exoplatform.ideall.client.event.file.DownloadFileHandler;
import org.exoplatform.ideall.client.event.file.DownloadZippedFolderEvent;
import org.exoplatform.ideall.client.event.file.DownloadZippedFolderHandler;
import org.exoplatform.ideall.client.model.ApplicationContext;
import org.exoplatform.ideall.client.model.File;
import org.exoplatform.ideall.client.model.configuration.Configuration;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.HTMLPane;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class DownloadContentForm implements RegisterEventHandlersHandler, DownloadFileHandler,
   DownloadZippedFolderHandler
{

   private String CONTEXT_DOWNLOAD = "/services/downloadcontent";

   private HandlerManager eventBus;

   private ApplicationContext context;

   private Handlers handlers;

   private HTMLPane htmlPane;

   public DownloadContentForm(HandlerManager eventBus, ApplicationContext context)
   {
      this.eventBus = eventBus;
      this.context = context;

      handlers = new Handlers(eventBus);
      eventBus.addHandler(RegisterEventHandlersEvent.TYPE, this);

      htmlPane = new HTMLPane();
      htmlPane.setWidth(1);
      htmlPane.setHeight(1);
      RootPanel.get().add(htmlPane, -100, -100);
   }

   public void onRegisterEventHandlers(RegisterEventHandlersEvent event)
   {
      handlers.addHandler(DownloadFileEvent.TYPE, this);
      handlers.addHandler(DownloadZippedFolderEvent.TYPE, this);
   }

   private void downloadResource()
   {
      String fileName = context.getSelectedItem().getPath();
      fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
      if (!(context.getSelectedItem() instanceof File))
      {
         fileName += ".zip";
      }

      String path = context.getSelectedItem().getPath();

      System.out.println("file name: [" + fileName + "]");
      System.out.println("path: [" + path + "]");

      String url = Configuration.getInstance().getContext() + CONTEXT_DOWNLOAD + "/" + fileName + "?repoPath=" + path;
      System.out.println("url: " + url);
      String iframe =
         "<iframe src=\"" + url
            + "\" frameborder=0 width=\"100%\" height=\"100%\" style=\"overflow:visible;\"></iframe>";
      htmlPane.setContents(iframe);
   }

   public void onDownloadFile(DownloadFileEvent event)
   {
      downloadResource();
   }

   public void onDownloadZippedFolder(DownloadZippedFolderEvent event)
   {
      downloadResource();
   }

}
