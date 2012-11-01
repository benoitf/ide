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
package org.exoplatform.ide.extension.java.jdi.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Timer;
import com.google.web.bindery.autobean.shared.AutoBean;

import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.gwtframework.commons.rest.AutoBeanUnmarshaller;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.ui.api.IsView;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedEvent;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler;
import org.exoplatform.ide.client.framework.websocket.WebSocket;
import org.exoplatform.ide.client.framework.websocket.WebSocketEventHandler;
import org.exoplatform.ide.client.framework.websocket.WebSocketException;
import org.exoplatform.ide.client.framework.websocket.MessageBus.Channels;
import org.exoplatform.ide.extension.java.jdi.client.events.DebuggerConnectedEvent;
import org.exoplatform.ide.extension.java.jdi.client.events.StopAppEvent;
import org.exoplatform.ide.extension.java.jdi.shared.ApplicationInstance;
import org.exoplatform.ide.extension.java.jdi.shared.DebuggerInfo;

/**
 * Created by The eXo Platform SAS.
 * @author <a href="mailto:vparfonov@exoplatform.com">Vitaly Parfonov</a>
 * @version $Id: $
*/
public class ReLaunchDebuggerPresenter implements ViewClosedHandler

{
   public interface Display extends IsView
   {

      HasClickHandlers getCancelButton();
   }

   private Display display;

   private final ApplicationInstance instance;

   /**
    * Used for subscribe/unsubscribe on receive WebSocket messages.
    */
   private final WebSocketEventHandler debuggerEventHandler; 

   public ReLaunchDebuggerPresenter(ApplicationInstance instance, WebSocketEventHandler webSocketSubscriber)
   {
      this.instance = instance;
      this.debuggerEventHandler = webSocketSubscriber;
      IDE.addHandler(ViewClosedEvent.TYPE, this);
   }

   /**
    * Bind display with presenter.
    */
   public void bindDisplay(Display d)
   {
      display = d;

      display.getCancelButton().addClickHandler(new ClickHandler()
      {

         @Override
         public void onClick(ClickEvent event)
         {
            IDE.fireEvent(new StopAppEvent());
            tryConnectDebuger.cancel();
            WebSocket.getInstance().messageBus().unsubscribe(Channels.DEBUGGER_EVENT.toString(), debuggerEventHandler);
            IDE.getInstance().closeView(display.asView().getId());
         }
      });
      
      tryConnectDebuger.scheduleRepeating(3000);
   }

   /**
    * @see org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler#onViewClosed(org.exoplatform.ide.client.framework.ui.api.event.ViewClosedEvent)
    */
   @Override
   public void onViewClosed(ViewClosedEvent event)
   {
      if (event.getView() instanceof Display)
      {
         display = null;
      }
   }

   protected void doRunDebugger()
   {
      AutoBean<DebuggerInfo> debuggerInfo = DebuggerExtension.AUTO_BEAN_FACTORY.create(DebuggerInfo.class);
      AutoBeanUnmarshaller<DebuggerInfo> unmarshaller = new AutoBeanUnmarshaller<DebuggerInfo>(debuggerInfo);
      try
      {
         boolean useWebSocketForCallback = false;
         final WebSocket ws = null;//WebSocket.getInstance(); TODO: temporary disable web-sockets
         if (ws != null && ws.getReadyState() == WebSocket.ReadyState.OPEN)
         {
            useWebSocketForCallback = true;
            ws.messageBus().subscribe(Channels.DEBUGGER_EVENT, debuggerEventHandler);
         }
         final boolean useWebSocket = useWebSocketForCallback;

         DebuggerClientService.getInstance().create(instance.getDebugHost(), instance.getDebugPort(), useWebSocket,
            new AsyncRequestCallback<DebuggerInfo>(unmarshaller)
            {
               @Override
               public void onSuccess(DebuggerInfo result)
               {
                  tryConnectDebuger.cancel();
                  IDE.getInstance().closeView(display.asView().getId());
                  IDE.eventBus().fireEvent(new DebuggerConnectedEvent(result));
               }

               @Override
               protected void onFailure(Throwable exception)
               {
//                  IDE.eventBus().fireEvent(new ExceptionThrownEvent(exception));
                  if (useWebSocket)
                  {
                     ws.messageBus().unsubscribe(Channels.DEBUGGER_EVENT, debuggerEventHandler);
                  }
               }
            });
      }
      catch (RequestException e)
      {
//         IDE.eventBus().fireEvent(new ExceptionThrownEvent(e));
      }
      catch (WebSocketException e)
      {
//       IDE.eventBus().fireEvent(new ExceptionThrownEvent(e));
      }

   }
   
   /**
    * A timer for checking events
    */
   private Timer tryConnectDebuger = new Timer()
   {
      @Override
      public void run()
      {
         doRunDebugger();
      }
   };


}
