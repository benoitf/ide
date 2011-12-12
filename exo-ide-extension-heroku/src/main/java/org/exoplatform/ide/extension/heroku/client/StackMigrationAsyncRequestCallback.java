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
package org.exoplatform.ide.extension.heroku.client;

import com.google.gwt.event.shared.HandlerManager;

import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.rest.copy.AsyncRequestCallback;
import org.exoplatform.gwtframework.commons.rest.copy.HTTPStatus;
import org.exoplatform.gwtframework.commons.rest.copy.ServerException;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.extension.heroku.client.login.LoggedInEvent;
import org.exoplatform.ide.extension.heroku.client.login.LoggedInHandler;
import org.exoplatform.ide.extension.heroku.client.login.LoginEvent;
import org.exoplatform.ide.extension.heroku.client.marshaller.StackMigrationResponse;
import org.exoplatform.ide.extension.heroku.client.marshaller.StackMigrationUnmarshaller;

/**
 * Asynchronous callback on migrate stack response.
 * 
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:  Aug 1, 2011 4:10:31 PM anya $
 *
 */
public abstract class StackMigrationAsyncRequestCallback extends AsyncRequestCallback<StackMigrationResponse>
{
   /**
    * Events handler.
    */
   private HandlerManager eventbus;

   /**
    * Handler of the {@link LoggedInEvent}.
    */
   private LoggedInHandler loggedInHandler;

   /**
    * @param eventBus event handlers manager
    * @param handler handler of the {@link LoggedInEvent}
    */
   public StackMigrationAsyncRequestCallback(HandlerManager eventBus, LoggedInHandler handler)
   {
      super(new StackMigrationUnmarshaller(new StackMigrationResponse()));
      this.eventbus = eventBus;
      this.loggedInHandler = handler;
      setEventBus(eventBus);
   }

   /**
    * @see org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback#onFailure(java.lang.Throwable)
    */
   @Override
   protected void onFailure(Throwable exception)
   {
      if (exception instanceof ServerException)
      {
         ServerException serverException = (ServerException)exception;
         if (HTTPStatus.OK == serverException.getHTTPStatus() && serverException.getMessage() != null
            && serverException.getMessage().contains("Authentication required"))
         {
            eventbus.addHandler(LoggedInEvent.TYPE, loggedInHandler);
            eventbus.fireEvent(new LoginEvent());
            return;
         }
      }
      IDE.fireEvent(new ExceptionThrownEvent(exception));
   }

}
