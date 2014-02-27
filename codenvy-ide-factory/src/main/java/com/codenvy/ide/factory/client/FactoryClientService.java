package com.codenvy.ide.factory.client;

import com.codenvy.api.factory.dto.AdvancedFactoryUrl;
import com.codenvy.ide.websocket.WebSocketException;
import com.codenvy.ide.websocket.rest.RequestCallback;

import javax.validation.constraints.NotNull;

/**
 * @author vzhukovskii@codenvy.com
 */
public interface FactoryClientService {
    public abstract void sendFactoryByEmail(@NotNull String email, @NotNull String message, @NotNull RequestCallback<Void> callback)
            throws WebSocketException;

    public abstract void getFactoryById(@NotNull String factoryId, @NotNull RequestCallback<AdvancedFactoryUrl> callback)
            throws WebSocketException;

    public abstract void acceptFactory(@NotNull AdvancedFactoryUrl factory, @NotNull RequestCallback<String> callback)
            throws WebSocketException;
}
