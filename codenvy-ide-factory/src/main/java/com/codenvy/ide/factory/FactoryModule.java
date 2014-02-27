package com.codenvy.ide.factory;

import com.codenvy.api.factory.FactoryStore;
import com.codenvy.api.factory.FactoryUrlValidator;
import com.codenvy.factory.FactoryUrlBaseValidator;
import com.codenvy.factory.storage.InMemoryFactoryStore;
import com.codenvy.ide.factory.server.Factory;
import com.codenvy.ide.factory.server.rest.FactoryExceptionMapper;
import com.codenvy.ide.factory.server.rest.FactoryService;
import com.codenvy.inject.DynaModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * @author vzhukovskii@codenvy.com
 */
@DynaModule
public class FactoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Factory.class);
        bind(FactoryService.class);
        bind(FactoryExceptionMapper.class).toInstance(new FactoryExceptionMapper());

        //platform api factory service
        bind(FactoryUrlValidator.class).to(FactoryUrlBaseValidator.class);
        bind(FactoryStore.class).to(InMemoryFactoryStore.class);

        Multibinder<FactoryStore> factoryStoreMultibinder = Multibinder.newSetBinder(binder(), FactoryStore.class);
        factoryStoreMultibinder.addBinding().to(InMemoryFactoryStore.class);

//        bind(com.codenvy.api.factory.FactoryService.class);
    }
}
