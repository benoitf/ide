/*
 * Copyright (C) 2012 eXo Platform SAS.
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
package org.exoplatform.ide.extension.googleappengine.client.deploy;

import com.google.gwt.http.client.RequestException;

import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.output.event.OutputEvent;
import org.exoplatform.ide.client.framework.output.event.OutputMessage.Type;
import org.exoplatform.ide.client.framework.util.ProjectResolver;
import org.exoplatform.ide.extension.googleappengine.client.GoogleAppEngineAsyncRequestCallback;
import org.exoplatform.ide.extension.googleappengine.client.GoogleAppEngineClientService;
import org.exoplatform.ide.extension.googleappengine.client.GoogleAppEngineExtension;
import org.exoplatform.ide.extension.googleappengine.client.GoogleAppEnginePresenter;
import org.exoplatform.ide.extension.googleappengine.client.login.LoggedInHandler;
import org.exoplatform.ide.extension.googleappengine.client.login.PerformOperationHandler;

/**
 * @author <a href="mailto:azhuleva@exoplatform.com">Ann Shumilova</a>
 * @version $Id: May 16, 2012 5:51:08 PM anya $
 * 
 */
public class DeployApplicationPresenter extends GoogleAppEnginePresenter implements DeployApplicationHandler
{
   private PerformOperationHandler performOperationHandler = new PerformOperationHandler()
   {

      @Override
      public void onPerformOperation(String email, String password, LoggedInHandler loggedInHandler)
      {
         deployApplication(email, password, loggedInHandler);
      }
   };

   public DeployApplicationPresenter()
   {
      IDE.addHandler(DeployApplicationEvent.TYPE, this);

      IDE.getInstance().addControl(new DeployApplicationControl());
   }

   /**
    * @see org.exoplatform.ide.extension.googleappengine.client.deploy.DeployApplicationHandler#onDeployApplication(org.exoplatform.ide.extension.googleappengine.client.deploy.DeployApplicationEvent)
    */
   @Override
   public void onDeployApplication(DeployApplicationEvent event)
   {
      if (currentProject != null && ProjectResolver.APP_ENGINE_JAVA.equals(currentProject.getProjectType()))
      {
         deployApplication(null, null, null);
      }
   }

   public void deployApplication(String email, String password, final LoggedInHandler loggedInHandler)
   {
      try
      {
         GoogleAppEngineClientService.getInstance().update(currentVfs.getId(), currentProject, null, email,
            password, new GoogleAppEngineAsyncRequestCallback<Object>(performOperationHandler, null)
            {

               @Override
               protected void onSuccess(Object result)
               {
                  if (loggedInHandler != null)
                  {
                     loggedInHandler.onLoggedIn();
                  }
                  IDE.fireEvent(new OutputEvent(GoogleAppEngineExtension.GAE_LOCALIZATION
                     .deployApplicationSuccess(currentProject.getName()), Type.INFO));
               }
            });
      }
      catch (RequestException e)
      {
         IDE.fireEvent(new ExceptionThrownEvent(e));
      }
   }
}