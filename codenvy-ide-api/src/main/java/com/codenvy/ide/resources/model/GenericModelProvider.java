/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.resources.model;

import com.codenvy.ide.api.resources.ModelProvider;
import com.codenvy.ide.rest.AsyncRequestFactory;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Model provider for generic Project
 *
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a>
 */
public class GenericModelProvider implements ModelProvider {

    private final EventBus            eventBus;
    private final AsyncRequestFactory asyncRequestFactory;

    /** Creates GenericModel provider */
    @Inject
    public GenericModelProvider(EventBus eventBus, AsyncRequestFactory asyncRequestFactory) {
        this.eventBus = eventBus;
        this.asyncRequestFactory = asyncRequestFactory;
    }

    /** {@inheritDoc} */
    @Override
    public Project createProjectInstance() {
        return new Project(eventBus, asyncRequestFactory);
    }

}
