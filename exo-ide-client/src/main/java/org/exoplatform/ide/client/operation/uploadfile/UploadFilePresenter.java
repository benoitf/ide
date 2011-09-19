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

package org.exoplatform.ide.client.operation.uploadfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.exoplatform.gwtframework.ui.client.dialog.Dialogs;
import org.exoplatform.ide.client.IDE;
import org.exoplatform.ide.client.IDELoader;
import org.exoplatform.ide.client.framework.configuration.ConfigurationReceivedSuccessfullyEvent;
import org.exoplatform.ide.client.framework.configuration.ConfigurationReceivedSuccessfullyHandler;
import org.exoplatform.ide.client.framework.configuration.IDEConfiguration;
import org.exoplatform.ide.client.framework.event.RefreshBrowserEvent;
import org.exoplatform.ide.client.framework.navigation.event.ItemsSelectedEvent;
import org.exoplatform.ide.client.framework.navigation.event.ItemsSelectedHandler;
import org.exoplatform.ide.client.framework.ui.api.IsView;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedEvent;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler;
import org.exoplatform.ide.client.framework.ui.upload.FileSelectedEvent;
import org.exoplatform.ide.client.framework.ui.upload.FileSelectedHandler;
import org.exoplatform.ide.client.framework.ui.upload.HasFileSelectedHandler;
import org.exoplatform.ide.client.framework.vfs.NodeTypeUtil;
import org.exoplatform.ide.client.model.util.IDEMimeTypes;
import org.exoplatform.ide.vfs.client.model.FileModel;
import org.exoplatform.ide.vfs.client.model.FolderModel;
import org.exoplatform.ide.vfs.shared.Item;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasValue;

/**
 * 
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */
public class UploadFilePresenter implements UploadFileHandler, ViewClosedHandler, ConfigurationReceivedSuccessfullyHandler, ItemsSelectedHandler
{

   public interface Display extends IsView
   {

      HasValue<String> getMimeTypeField();

      void setSelectedMimeType(String mimeType);

      void setMimeTypes(String[] mimeTypes);

      void setMimeTypeFieldEnabled(boolean enabled);

      void setHiddenFields(String location, String mimeType, String nodeType, String jcrContentNodeType);

      HasClickHandlers getOpenButton();

      void setOpenButtonEnabled(boolean enabled);

      HasClickHandlers getCloseButton();

      FormPanel getUploadForm();

      HasValue<String> getFileNameField();

      HasFileSelectedHandler getFileUploadInput();

   }

   private Display display;
   
   private IDEConfiguration configuration;

   private String file_name;

   private String file_mimeType;   
   
   private List<Item> selectedItems = new ArrayList<Item>();

   public UploadFilePresenter()
   {
      IDE.getInstance().addControl(new UploadFileCommand());
      IDE.EVENT_BUS.addHandler(UploadFileEvent.TYPE, this);
      IDE.EVENT_BUS.addHandler(ViewClosedEvent.TYPE, this);
      IDE.EVENT_BUS.addHandler(ConfigurationReceivedSuccessfullyEvent.TYPE, this);
      IDE.EVENT_BUS.addHandler(ItemsSelectedEvent.TYPE, this);
   }
   
   @Override
   public void onConfigurationReceivedSuccessfully(ConfigurationReceivedSuccessfullyEvent event)
   {
      configuration = event.getConfiguration();
   }
   
   @Override
   public void onItemsSelected(ItemsSelectedEvent event)
   {
      selectedItems = event.getSelectedItems();
   }   

   @Override
   public void onViewClosed(ViewClosedEvent event)
   {
      if (event.getView() instanceof Display)
      {
         display = null;
      }
   }

   @Override
   public void onUploadFile(UploadFileEvent event)
   {
      if (display != null)
      {
         return;
      }

      display = GWT.create(Display.class);
      IDE.getInstance().openView(display.asView());
      bindDisplay();
   }

   private void bindDisplay()
   {
      display.getUploadForm().setMethod(FormPanel.METHOD_POST);
      display.getUploadForm().setEncoding(FormPanel.ENCODING_MULTIPART);
      display.getUploadForm().setAction(configuration.getUploadServiceContext() + "/");

      display.setOpenButtonEnabled(false);
      display.setMimeTypeFieldEnabled(false);
      display.getFileUploadInput().addFileSelectedHandler(fileSelectedHandler);
      display.getMimeTypeField().addValueChangeHandler(mimeTypeChangedHandler);

      display.getOpenButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            doUploadFile();
         }
      });

      display.getCloseButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            IDE.getInstance().closeView(display.asView().getId());
         }
      });

      display.getUploadForm().addSubmitHandler(new SubmitHandler()
      {
         public void onSubmit(SubmitEvent event)
         {
            submit(event);
         }
      });

      display.getUploadForm().addSubmitCompleteHandler(new SubmitCompleteHandler()
      {
         public void onSubmitComplete(SubmitCompleteEvent event)
         {
            submitComplete(event.getResults());
         }
      });      
   }
   
   private FileSelectedHandler fileSelectedHandler = new FileSelectedHandler()
   {
      @Override
      public void onFileSelected(FileSelectedEvent event)
      {
         String file = event.getFileName();
         file = file.replace('\\', '/');

         if (file.indexOf('/') >= 0)
         {
            file = file.substring(file.lastIndexOf("/") + 1);
         }

         display.getFileNameField().setValue(file);
         file_name = file;
         display.setMimeTypeFieldEnabled(true);

         List<String> mimeTypes = IDEMimeTypes.getSupportedMimeTypes();
         Collections.sort(mimeTypes);

         List<String> proposalMimeTypes = IDEMimeTypes.getMimeTypes(event.getFileName());

         String[] valueMap = mimeTypes.toArray(new String[0]);

         display.setMimeTypes(valueMap);

         if (proposalMimeTypes != null && proposalMimeTypes.size() > 0)
         {
            String mimeTYpe = proposalMimeTypes.get(0);
            file_mimeType = mimeTYpe;
            display.setSelectedMimeType(mimeTYpe);
            display.setOpenButtonEnabled(true);
         }
         else
         {
            display.setOpenButtonEnabled(false);
         }
      }
   };
   
   ValueChangeHandler<String> mimeTypeChangedHandler = new ValueChangeHandler<String>()
   {
      @Override
      public void onValueChange(ValueChangeEvent<String> event)
      {
         if (display.getMimeTypeField().getValue() != null && display.getMimeTypeField().getValue().length() > 0)
         {
            display.setOpenButtonEnabled(true);
         }
         else
         {
            display.setOpenButtonEnabled(false);
         }
      }
   };
   
   private void doUploadFile()
   {
      String mimeType = display.getMimeTypeField().getValue();

      if (mimeType == null || "".equals(mimeType))
      {
         return;
      }

      String contentNodeType = NodeTypeUtil.getContentNodeType(mimeType);

      String fileName = display.getFileNameField().getValue();
      if (fileName.contains("/"))
      {
         fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
      }

      Item item = selectedItems.get(0);
      String href = item.getPath();
      if (item instanceof FileModel)
      {
         href = href.substring(0, href.lastIndexOf("/") + 1);
      }
      href += URL.encodePathSegment(fileName);

      
      
      display.setHiddenFields(href, mimeType, "", contentNodeType);
      display.getUploadForm().submit();
   }   
   
   protected void submit(SubmitEvent event)
   {
      IDELoader.getInstance().show();
   }

   private void submitComplete(String uploadServiceResponse)
   {
      IDELoader.getInstance().hide();

      String responseOk = checkResponseOk(uploadServiceResponse);
      if (responseOk != null)
      {
         Dialogs.getInstance().showError(responseOk);
         return;
      }
      completeUpload(uploadServiceResponse);
   }
   
   protected String errorMessage()
   {
      return IDE.ERRORS_CONSTANT.uploadFileUploadingFailure();
   }   

   /**
    * Check response is Ok.
    * If response is Ok, return null,
    * else return error message
    * 
    * @param uploadServiceResponse
    * @return
    */
   private String checkResponseOk(String uploadServiceResponse)
   {
      boolean matches =
         uploadServiceResponse.matches("^<ERROR>(.*)</ERROR>$")
            || uploadServiceResponse.matches("^<error>(.*)</error>$");

      if (!gotError(uploadServiceResponse, matches))
      {
         return null;
      }

      if (matches)
      {
         String errorMsg = uploadServiceResponse.substring("<error>".length());
         errorMsg = errorMsg.substring(0, errorMsg.length() - "</error>".length());

         return errorMsg;
      }
      else
      {
         return errorMessage();
      }
   }
   
   protected boolean gotError(String uploadServiceResponse, boolean matches)
   {
      return uploadServiceResponse == null || uploadServiceResponse.length() > 0 || matches;
   }
   
   protected void completeUpload(String response)
   {
      IDE.getInstance().closeView(display.asView().getId());
      
      if (selectedItems.get(0) instanceof FileModel) {
         IDE.EVENT_BUS.fireEvent(new RefreshBrowserEvent(((FileModel)selectedItems.get(0)).getParent()));
      } else if (selectedItems.get(0) instanceof FolderModel) {
         IDE.EVENT_BUS.fireEvent(new RefreshBrowserEvent((FolderModel)selectedItems.get(0)));
      }
      
//      eventBus.fireEvent(new RefreshBrowserEvent(folder));
//      eventBus.fireEvent(new RefreshBrowserEvent(baseFolder, result));
      
   }   


}
