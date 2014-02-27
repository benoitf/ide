package com.codenvy.ide.factory.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.factory.client.handle.FactoryHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * @author vzhukovskii@codenvy.com
 */
@Singleton
@Extension(title = "Factory service.", version = "3.0.0")
public class FactoryExtension {
    private ResourceProvider    resourceProvider;
    private NotificationManager notificationManager;
    private String              restContext;

    @Inject
    public FactoryExtension(ResourceProvider resourceProvider,
                            NotificationManager notificationManager,
                            @Named("restContext") String restContext,
                            FactoryHandler factoryHandler) {
        this();
        this.resourceProvider = resourceProvider;
        this.notificationManager = notificationManager;
        this.restContext = restContext;

        factoryHandler.checkQueryParamToProcessFactory();
    }

    /** For test use only. */
    public FactoryExtension() {
    }
}
