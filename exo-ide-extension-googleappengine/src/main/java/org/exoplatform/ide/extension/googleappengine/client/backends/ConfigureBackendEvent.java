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
package org.exoplatform.ide.extension.googleappengine.client.backends;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event occurs, when user tries to configure backend.
 *
 * @author <a href="mailto:azhuleva@exoplatform.com">Ann Shumilova</a>
 * @version $Id: May 30, 2012 5:24:39 PM anya $
 */
public class ConfigureBackendEvent extends GwtEvent<ConfigureBackendHandler> {
    /** Type, used to register the event. */
    public static final GwtEvent.Type<ConfigureBackendHandler> TYPE = new GwtEvent.Type<ConfigureBackendHandler>();

    private String backendName;

    public ConfigureBackendEvent(String backendName) {
        this.backendName = backendName;
    }

    /** @see com.google.gwt.event.shared.GwtEvent#getAssociatedType() */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ConfigureBackendHandler> getAssociatedType() {
        return TYPE;
    }

    /** @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler) */
    @Override
    protected void dispatch(ConfigureBackendHandler handler) {
        handler.onConfigureBackend(this);
    }

    /** @return the backendName */
    public String getBackendName() {
        return backendName;
    }
}