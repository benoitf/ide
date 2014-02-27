package com.codenvy.ide.factory.client.handle;

import com.codenvy.api.factory.dto.AdvancedFactoryUrl;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.parts.ConsolePart;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.collections.StringMap;
import com.codenvy.ide.commons.exception.ExceptionThrownEvent;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.factory.client.FactoryClientService;
import com.codenvy.ide.factory.client.marshaller.AdvancedFactoryUnmarshallerWS;
import com.codenvy.ide.factory.server.Factory;
import com.codenvy.ide.factory.shared.Param;
import com.codenvy.ide.websocket.MessageBus;
import com.codenvy.ide.websocket.WebSocketException;
import com.codenvy.ide.websocket.events.MessageHandler;
import com.codenvy.ide.websocket.rest.RequestCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vzhukovskii@codenvy.com
 */
@Singleton
public class FactoryHandler {

    private DtoFactory           dtoFactory;
    private FactoryClientService factoryService;
    private EventBus             eventBus;
    private NotificationManager  notificationManager;
    private MessageBus           messageBus;
    private ConsolePart          console;
    private ResourceProvider resourceProvider;

    @Inject
    public FactoryHandler(DtoFactory dtoFactory, FactoryClientService factoryService, EventBus eventBus,
                          NotificationManager notificationManager, MessageBus messageBus, ConsolePart console,
                          ResourceProvider resourceProvider) {
        this.dtoFactory = dtoFactory;
        this.factoryService = factoryService;
        this.eventBus = eventBus;
        this.notificationManager = notificationManager;
        this.messageBus = messageBus;
        this.console = console;
        this.resourceProvider = resourceProvider;
    }

    /**
     * Entry point to factory accept. This method call require variable "initParams" somewhere in DOM of main page.
     * If var initParams is setted in Window object it will be used, otherwise in case if initParams doesn't exist
     * method try to fetch url query parameters
     */
    // @formatter:off
    private native String getRawInitParams() /*-{
        try {
            if (!$wnd["initParams"]) {
            } else {
                return $wnd["initParams"];
            }
        } catch (e) {
            console.log("ERROR > " + e.message);
        }
    }-*/;
    // @formatter:on

    public void checkQueryParamToProcessFactory() {
        checkWebsocketOpenStateTimer.scheduleRepeating(500);
    }

    Timer checkWebsocketOpenStateTimer = new Timer() {
        @Override
        public void run() {
            GWT.log("check: " + Math.random());
            if (messageBus.getReadyState() == MessageBus.ReadyState.OPEN) {
                StringMap<Array<String>> queryMap = getQueryMap(getRawInitParams());

                if (isFactorySimple(queryMap)) {
                    AdvancedFactoryUrl factory = buildFactoryObject(queryMap);
                    processFactory(factory);
                } else if (isFactoryAdvanced(queryMap)) {
                    getAdvancedFactoryById(queryMap.get(Param.ID.toString()).get(0));
                }

                cancel();
            }
        }
    };

    private void getAdvancedFactoryById(String ID) {
        try {
            factoryService.getFactoryById(ID, new RequestCallback<AdvancedFactoryUrl>(new AdvancedFactoryUnmarshallerWS(dtoFactory)) {
                @Override
                protected void onSuccess(AdvancedFactoryUrl factory) {
                    processFactory(factory);
                }

                @Override
                protected void onFailure(Throwable e) {
//                    eventBus.fireEvent(new ExceptionThrownEvent(e));
                    notificationManager.showNotification(new Notification("Failed to get factory description by setted ID",
                                                                          Notification.Type.ERROR));
                }
            });
        } catch (WebSocketException e) {
            eventBus.fireEvent(new ExceptionThrownEvent(e));
            Notification notification = new Notification(e.getMessage(), Notification.Type.ERROR);
            notificationManager.showNotification(notification);
        }
    }

    private void processFactory(AdvancedFactoryUrl factory) {
        subscribeToFactoryEventsChannel();

        try {
            factoryService.acceptFactory(factory, new RequestCallback<String>() {
                @Override
                protected void onSuccess(String s) {
                    unSubscribeFromFactoryEventsChannel();

                    resourceProvider.showListProjects();
                }

                @Override
                protected void onFailure(Throwable throwable) {
                    unSubscribeFromFactoryEventsChannel();
                }
            });
        } catch (WebSocketException e) {
            unSubscribeFromFactoryEventsChannel();
        }
    }

    // Message Bus Handlers ----------------
    private MessageHandler processFactoryHandler = new MessageHandler() {
        @Override
        public void onMessage(String message) {
            notificationManager.showNotification(new Notification(message, Notification.Type.INFO));
        }
    };

    // Commons -----------------------------

    private void subscribeToFactoryEventsChannel() {
        try {
            if (!messageBus.isHandlerSubscribed(processFactoryHandler, Factory.WS_FACTORY_EVENTS_CHANNEL)) {
                messageBus.subscribe(Factory.WS_FACTORY_EVENTS_CHANNEL, processFactoryHandler);
            }
        } catch (WebSocketException e) {
            notificationManager.showNotification(new Notification("Failed to subscribe to events channel.", Notification.Type.WARNING));
        }
    }

    private void unSubscribeFromFactoryEventsChannel() {
        try {
            if (messageBus.isHandlerSubscribed(processFactoryHandler, Factory.WS_FACTORY_EVENTS_CHANNEL)) {
                messageBus.unsubscribe(Factory.WS_FACTORY_EVENTS_CHANNEL, processFactoryHandler);
            }
        } catch (WebSocketException e) {
            //ignore
        }
    }

    private AdvancedFactoryUrl buildFactoryObject(StringMap<Array<String>> queryMap) {
        AdvancedFactoryUrl factory = dtoFactory.createDto(AdvancedFactoryUrl.class);

        if (paramExist(Param.V, queryMap)) {
            factory.setV(getParamStr(Param.V, queryMap));
        }
        if (paramExist(Param.VCS_URL, queryMap)) {
            factory.setVcsurl(getParamStr(Param.VCS_URL, queryMap));
        }
        if (paramExist(Param.VCS, queryMap)) {
            factory.setVcs(getParamStr(Param.VCS, queryMap));
        }
        if (paramExist(Param.COMMIT_ID, queryMap)) {
            factory.setCommitid(getParamStr(Param.COMMIT_ID, queryMap));
        }
        if (paramExist(Param.ACTION, queryMap)) {
            factory.setAction(getParamStr(Param.ACTION, queryMap));
        }
        if (paramExist(Param.OPEN_FILE, queryMap)) {
            factory.setOpenfile(getParamStr(Param.OPEN_FILE, queryMap));
        }
        if (paramExist(Param.VCS_INFO, queryMap)) {
            factory.setVcsinfo(Boolean.parseBoolean(getParamStr(Param.VCS_INFO, queryMap)));
        }
        if (paramExist(Param.ORG_ID, queryMap)) {
            factory.setOrgid(getParamStr(Param.ORG_ID, queryMap));
        }
        if (paramExist(Param.AFFILIATE_ID, queryMap)) {
            factory.setAffiliateid(getParamStr(Param.AFFILIATE_ID, queryMap));
        }
        if (paramExist(Param.VCS_BRANCH, queryMap)) {
            factory.setVcsbranch(getParamStr(Param.VCS_BRANCH, queryMap));
        }

        Map<String, String> attributes = new HashMap<String, String>(2);
        if (paramExist(Param.PROFILE_ATTRIBUTES_PNAME, queryMap)) {
            attributes.put(Param.PROFILE_ATTRIBUTES_PNAME.toString(), getParamStr(Param.PROFILE_ATTRIBUTES_PNAME, queryMap));
        }
        if (paramExist(Param.PROFILE_ATTRIBUTES_PTYPE, queryMap)) {
            attributes.put(Param.PROFILE_ATTRIBUTES_PTYPE.toString(), getParamStr(Param.PROFILE_ATTRIBUTES_PTYPE, queryMap));
        }

        factory.setProjectattributes(attributes);

        return factory;
    }

    private boolean isFactorySimple(StringMap<Array<String>> queryMap) {
        return paramExist(Param.V, queryMap)
               && getParamStr(Param.V, queryMap).equals("1.0")
               && !paramExist(Param.ID, queryMap);
    }

    private boolean isFactoryAdvanced(StringMap<Array<String>> queryMap) {
        return paramExist(Param.ID, queryMap);
    }

    private StringMap<Array<String>> getQueryMap(String rawInitParams) {
        StringMap<Array<String>> out = Collections.createStringMap();

        if (rawInitParams != null && !rawInitParams.isEmpty()) {
            String qs = rawInitParams.startsWith("?") ? rawInitParams.substring(1) : rawInitParams;
            for (String kvPair : qs.split("&")) {
                String[] kv = kvPair.split("=", 2);
                if (kv[0].length() == 0) {
                    continue;
                }

                Array<String> values = out.get(kv[0]);
                if (values == null) {
                    values = Collections.<String>createArray();
                    out.put(kv[0], values);
                }

                values.add(kv.length > 1 ? URL.decode(kv[1]) : "");
            }
        }

        return out;
    }

    private boolean paramExist(Param param, StringMap<Array<String>> queryMap) {
        return queryMap.containsKey(param.toString())
               && queryMap.get(param.toString()) != null
               && queryMap.get(param.toString()).get(0) != null;
    }

    private String getParamStr(Param param, StringMap<Array<String>> queryMap) {
        if (!paramExist(param, queryMap)) {
            return null;
        }

        return queryMap.get(param.toString()).get(0);
    }
}
