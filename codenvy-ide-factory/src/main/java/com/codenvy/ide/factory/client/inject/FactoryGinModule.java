package com.codenvy.ide.factory.client.inject;

import com.codenvy.ide.api.extension.ExtensionGinModule;
import com.codenvy.ide.factory.client.FactoryClientService;
import com.codenvy.ide.factory.client.FactoryClientServiceImpl;
import com.codenvy.ide.factory.client.handle.FactoryHandler;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * @author vzhukovskii@codenvy.com
 */
@ExtensionGinModule
public class FactoryGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(FactoryHandler.class).in(Singleton.class);
        bind(FactoryClientService.class).to(FactoryClientServiceImpl.class).in(Singleton.class);
    }
}
