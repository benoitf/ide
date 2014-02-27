package com.codenvy.ide.factory.client.marshaller;

import com.codenvy.api.factory.dto.AdvancedFactoryUrl;
import com.codenvy.ide.commons.exception.UnmarshallerException;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.websocket.Message;
import com.codenvy.ide.websocket.rest.Unmarshallable;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * @author vzhukovskii@codenvy.com
 */
public class AdvancedFactoryUnmarshallerWS implements Unmarshallable<AdvancedFactoryUrl> {
    private AdvancedFactoryUrl factory;

    private DtoFactory dtoFactory;

    public AdvancedFactoryUnmarshallerWS(DtoFactory dtoFactory) {
        this.dtoFactory = dtoFactory;
    }

    @Override
    public void unmarshal(Message message) throws UnmarshallerException {
        JSONObject jsonObj = JSONParser.parseStrict(message.getBody()).isObject();

        if (jsonObj == null) {
            return;
        }

        factory = dtoFactory.createDtoFromJson(jsonObj.toString(), AdvancedFactoryUrl.class);
    }

    @Override
    public AdvancedFactoryUrl getPayload() {
        return factory;
    }
}
