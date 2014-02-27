package com.codenvy.ide.factory.client;

import com.codenvy.api.factory.dto.AdvancedFactoryUrl;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.factory.client.handle.FactoryAcceptStatusHandler;
import com.codenvy.ide.rest.HTTPHeader;
import com.codenvy.ide.ui.loader.Loader;
import com.codenvy.ide.util.Utils;
import com.codenvy.ide.websocket.MessageBuilder;
import com.codenvy.ide.websocket.MessageBus;
import com.codenvy.ide.websocket.WebSocketException;
import com.codenvy.ide.websocket.rest.RequestCallback;
import com.google.gwt.http.client.RequestBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;

import javax.validation.constraints.NotNull;

import static com.google.gwt.http.client.URL.encode;

/**
 * @author vzhukovskii@codenvy.com
 */
@Singleton
public class FactoryClientServiceImpl implements FactoryClientService {
    public static final String SEND_FACTORY      = "/send";
    public static final String GET_FACTORY_BY_ID = "/api/factory";
    public static final String ACCEPT_FACTORY    = "/accept";
//    private final Loader                      loader;
    private final MessageBus                  messageBus;
    private final EventBus                    eventBus;
    private final FactoryLocalizationConstant constant;
    private final DtoFactory                  dtoFactory;
    private final String                      factoryServicePath;

    @Inject
    protected FactoryClientServiceImpl(/*@Named("restContext") String baseHttpUrl,*/
                                       /*Loader loader,*/
                                       MessageBus messageBus,
                                       EventBus eventBus,
                                       FactoryLocalizationConstant constant,
                                       DtoFactory dtoFactory) {
//        this.loader = loader;
        this.messageBus = messageBus;
        this.eventBus = eventBus;
        this.constant = constant;
        this.dtoFactory = dtoFactory;
        this.factoryServicePath = /*baseHttpUrl + */"/factory"/* + Utils.getWorkspaceId()*/;
    }

    @Override
    public void sendFactoryByEmail(@NotNull String email, @NotNull String message, @NotNull RequestCallback<Void> callback)
            throws WebSocketException {
        String url = factoryServicePath + SEND_FACTORY;
        String data = "email=" + encode(email) + "&message=" + encode(message);

//        callback.setLoader(loader);

        MessageBuilder builder = new MessageBuilder(RequestBuilder.POST, url);
        builder.header(HTTPHeader.CONTENT_TYPE, MimeType.APPLICATION_FORM_URLENCODED);
        builder.data(data);

        messageBus.send(builder.build(), callback);
    }

    @Override
    public void getFactoryById(@NotNull String factoryId, @NotNull RequestCallback<AdvancedFactoryUrl> callback)
            throws WebSocketException {
        String url = GET_FACTORY_BY_ID + "/" + factoryId; // api request

//        callback.setLoader(loader);

        MessageBuilder builder = new MessageBuilder(RequestBuilder.GET, url);
        builder.header(HTTPHeader.ACCEPT, MimeType.APPLICATION_JSON);

        messageBus.send(builder.build(), callback);
    }

    @Override
    public void acceptFactory(@NotNull AdvancedFactoryUrl factory, @NotNull RequestCallback<String> callback) throws WebSocketException {
        String url = factoryServicePath + ACCEPT_FACTORY;

//        callback.setLoader(loader);
        callback.setStatusHandler(new FactoryAcceptStatusHandler(eventBus, constant));

        MessageBuilder builder = new MessageBuilder(RequestBuilder.POST, url);
        builder.data(dtoFactory.toJson(factory)).header(HTTPHeader.CONTENT_TYPE, MimeType.APPLICATION_JSON);

        messageBus.send(builder.build(), callback);
    }
}
