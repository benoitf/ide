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
package org.exoplatform.ide.extension.heroku.client.login;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event occurs, when user tries to switch Heroku account. Implement {@link SwitchAccountHandler} to handle the event.
 *
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id: SwitchAccountEvent.java Jun 17, 2011 5:04:47 PM vereshchaka $
 */
public class SwitchAccountEvent extends GwtEvent<SwitchAccountHandler> {

    /** Type used to register this event. */
    public static final GwtEvent.Type<SwitchAccountHandler> TYPE = new GwtEvent.Type<SwitchAccountHandler>();

    /** @see com.google.gwt.event.shared.GwtEvent#getAssociatedType() */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SwitchAccountHandler> getAssociatedType() {
        return TYPE;
    }

    /** @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler) */
    @Override
    protected void dispatch(SwitchAccountHandler handler) {
        handler.onSwitchAccount(this);
    }

}