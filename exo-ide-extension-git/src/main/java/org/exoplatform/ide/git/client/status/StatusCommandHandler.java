/*
 * Copyright (C) 2011 eXo Platform SAS.
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
package org.exoplatform.ide.git.client.status;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.gwt.resources.client.ImageResource;

import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.gwtframework.ui.client.component.TreeIconPosition;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.navigation.event.AddItemTreeIconEvent;
import org.exoplatform.ide.client.framework.navigation.event.FolderRefreshedEvent;
import org.exoplatform.ide.client.framework.navigation.event.FolderRefreshedHandler;
import org.exoplatform.ide.client.framework.output.event.OutputEvent;
import org.exoplatform.ide.client.framework.output.event.OutputMessage;
import org.exoplatform.ide.client.framework.project.ProjectClosedEvent;
import org.exoplatform.ide.client.framework.project.ProjectClosedHandler;
import org.exoplatform.ide.client.framework.project.ProjectOpenedEvent;
import org.exoplatform.ide.client.framework.project.ProjectOpenedHandler;
import org.exoplatform.ide.client.framework.project.api.FolderOpenedEvent;
import org.exoplatform.ide.client.framework.project.api.FolderOpenedHandler;
import org.exoplatform.ide.git.client.GitClientBundle;
import org.exoplatform.ide.git.client.GitClientService;
import org.exoplatform.ide.git.client.GitExtension;
import org.exoplatform.ide.git.client.GitPresenter;
import org.exoplatform.ide.git.client.marshaller.StatusResponse;
import org.exoplatform.ide.git.client.marshaller.StatusResponseUnmarshaller;
import org.exoplatform.ide.git.shared.GitFile;
import org.exoplatform.ide.vfs.client.model.FolderModel;
import org.exoplatform.ide.vfs.client.model.ProjectModel;
import org.exoplatform.ide.vfs.shared.Folder;
import org.exoplatform.ide.vfs.shared.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler to process actions with displaying the status of the Git work tree.
 *
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id: Mar 28, 2011 3:58:20 PM anya $
 */
public class StatusCommandHandler extends GitPresenter implements ShowWorkTreeStatusHandler, FolderRefreshedHandler, ProjectOpenedHandler, ProjectClosedHandler, FolderOpenedHandler
{
   /**
    * Store the status of the working tree (changed, untracked files).
    * Status will be checked once, only when expands project item in Project Explorer.
    */
   private StatusResponse workingTreeStatus;
   
   private ProjectModel openedProject;

   public StatusCommandHandler()
   {
      IDE.addHandler(ShowWorkTreeStatusEvent.TYPE, this);
      IDE.addHandler(FolderRefreshedEvent.TYPE, this);
      IDE.addHandler(ProjectOpenedEvent.TYPE, this);
      IDE.addHandler(FolderOpenedEvent.TYPE, this);
      IDE.addHandler(ProjectClosedEvent.TYPE, this);
   }

   /** @see org.exoplatform.ide.git.client.status.ShowWorkTreeStatusHandler#onShowWorkTreeStatus(org.exoplatform.ide.git.client.status.ShowWorkTreeStatusEvent) */
   @Override
   public void onShowWorkTreeStatus(ShowWorkTreeStatusEvent event)
   {
      if (makeSelectionCheck())
      {
         getStatusText(getSelectedProject(), selectedItem);
      }
   }

   /**
    * Get the status for Git work tree and display it on success.
    *
    * @param item
    *    item in work tree
    */
   private void getStatusText(ProjectModel project, Item item)
   {
      if (project == null)
      {
         return;
      }
      String[] fileFilter = null;
      if (item instanceof Folder)
      {
         // Remove last "/" from path:
         String path =
            item.getPath().endsWith("/") ? item.getPath().substring(0, item.getPath().length() - 1) : item.getPath();
         path = URL.decodePathSegment(path);
         // Check selected item in workspace tree is not the root of the Git repository tree:
         if (!(item instanceof ProjectModel))
         {
            // Add filter to display status for the selected folder:
            path = (path.startsWith("/")) ? path.replaceFirst("/", "") : "";
            fileFilter = new String[]{path};
         }
      }
      try
      {
         GitClientService.getInstance().statusText(vfs.getId(), project.getId(), false, fileFilter,
            new AsyncRequestCallback<StatusResponse>(new StatusResponseUnmarshaller(new StatusResponse(), true))
            {

               @Override
               protected void onSuccess(StatusResponse result)
               {
                  if (result.getWorkTreeStatus() == null)
                  {
                     return;
                  }
                                    
                  String status = result.getWorkTreeStatus();
                  status = status.replace("\n", "<br>");
                  IDE.fireEvent(new OutputEvent(status, OutputMessage.Type.GIT));
               }

               @Override
               protected void onFailure(Throwable exception)
               {
                  String errorMessage =
                     (exception.getMessage() != null) ? exception.getMessage() : GitExtension.MESSAGES.statusFailed();
                  IDE.fireEvent(new OutputEvent(errorMessage, OutputMessage.Type.GIT));
               }
            });
      }
      catch (RequestException e)
      {
         String errorMessage = (e.getMessage() != null) ? e.getMessage() : GitExtension.MESSAGES.statusFailed();
         IDE.fireEvent(new OutputEvent(errorMessage, OutputMessage.Type.GIT));
      }
   }

   /** @see org.exoplatform.ide.client.framework.navigation.event.FolderRefreshedHandler#onFolderRefreshed(org.exoplatform.ide.client.framework.navigation.event.FolderRefreshedEvent) */
   @Override
   public void onFolderRefreshed(FolderRefreshedEvent event)
   {
      FolderModel folder = (FolderModel)event.getFolder();
      if (folder.getChildren().getItems().isEmpty() || folder.getId() == null || folder.getId().isEmpty())
      {
         return;
      }

      getStatus(folder, true, new ArrayList<Item>());
   }

   /**
    * Get the files in different state of Git cycle and mark them in browser tree.
    *
    * @param folder
    *    folder to be updated
    */
   private void getStatus(final FolderModel folder, boolean forced, final List<Item> additionalItems)
   {
      if (!folder.getId().equals(openedProject.getId()) && !forced)
      {
         addItemsTreeIcons(folder, additionalItems);
         return;
      }
      
      try
      {
         GitClientService.getInstance().status(vfs.getId(), openedProject.getId(),
            new AsyncRequestCallback<StatusResponse>(new StatusResponseUnmarshaller(new StatusResponse(), false))
            {
               @Override
               protected void onSuccess(StatusResponse result)
               {
                  workingTreeStatus = result;
                  addItemsTreeIcons(folder, additionalItems);
               }

               @Override
               protected void onFailure(Throwable exception)
               {
               }
            });
      }
      catch (RequestException ignored)
      {
      }
   }

   /**
    * Update icons for all items in the specified folder.
    *
    * @param project
    *    project
    * @param folder
    *    folder to be updated
    */
   private void addItemsTreeIcons(FolderModel folder, List<Item> additionalItems)
   {
      if (workingTreeStatus == null)
      {
         return;
      }

      Map<Item, Map<TreeIconPosition, ImageResource>> treeNodesToUpdate =
         new HashMap<Item, Map<TreeIconPosition, ImageResource>>();

      List<Item> itemsToCheck = new ArrayList<Item>();

      itemsToCheck.add(folder);
      itemsToCheck.addAll(folder.getChildren().getItems());
      itemsToCheck.addAll(additionalItems);
      
      for (Item item : itemsToCheck)
      {
         if (item instanceof FolderModel && ((FolderModel)item).getChildren().getItems().isEmpty())
         {
            continue;
         }
         
         String path = URL.decodePathSegment(item.getPath());
         String pattern = path.replaceFirst(openedProject.getPath(), "");
         pattern = (pattern.startsWith("/")) ? pattern.replaceFirst("/", "") : pattern;
         Map<TreeIconPosition, ImageResource> map = new HashMap<TreeIconPosition, ImageResource>();
         if (pattern.length() == 0 || "/".equals(pattern))
         {
            map.put(TreeIconPosition.BOTTOMRIGHT, GitClientBundle.INSTANCE.repositoryRoot());
         }
         else if (contains(workingTreeStatus.getChangedNotCommited(), pattern))
         {
            map.put(TreeIconPosition.BOTTOMRIGHT, GitClientBundle.INSTANCE.itemNotCommited());
         }
         else if (contains(workingTreeStatus.getChangedNotUpdated(), pattern))
         {
            map.put(TreeIconPosition.BOTTOMRIGHT, GitClientBundle.INSTANCE.itemChanged());
         }
         else if (contains(workingTreeStatus.getUntracked(), pattern))
         {
            map.put(TreeIconPosition.BOTTOMRIGHT, GitClientBundle.INSTANCE.itemNew());
         }
         else
         {
            map.put(TreeIconPosition.BOTTOMRIGHT, GitClientBundle.INSTANCE.itemInRepository());            
         }
         treeNodesToUpdate.put(item, map);
      }
      IDE.fireEvent(new AddItemTreeIconEvent(treeNodesToUpdate));
   }

   /**
    * Check whether files from Git status contain the match with pointed pattern.
    *
    * @param files
    *    files in status
    * @param pattern
    *    pattern to compare
    * @return pattern matchers one of the files in the list or not
    */
   private boolean contains(List<GitFile> files, String pattern)
   {
      for (GitFile file : files)
      {
         if (pattern.equals(file.getPath()))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public void onProjectOpened(ProjectOpenedEvent event)
   {
      openedProject = event.getProject();
      getStatus(openedProject, true, new ArrayList<Item>());
   }

   @Override
   public void onFolderOpened(FolderOpenedEvent event)
   {
      getStatus(event.getFolder(), false, event.getChildren());
   }

   @Override
   public void onProjectClosed(ProjectClosedEvent event)
   {
      openedProject = null;
   }
   
}
