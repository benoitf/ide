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
package org.exoplatform.ide.extension.googleappengine.client.cron;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event occurs, when user tries to update cron.
 *
 * @author <a href="mailto:azhuleva@exoplatform.com">Ann Shumilova</a>
 * @version $Id: May 25, 2012 4:19:38 PM anya $
 */
public class UpdateCronEvent extends GwtEvent<UpdateCronHandler> {
    /** Type, used to register the event. */
    public static final GwtEvent.Type<UpdateCronHandler> TYPE = new GwtEvent.Type<UpdateCronHandler>();

    /** @see com.google.gwt.event.shared.GwtEvent#getAssociatedType() */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateCronHandler> getAssociatedType() {
        return TYPE;
    }

    /** @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler) */
    @Override
    protected void dispatch(UpdateCronHandler handler) {
        handler.onUpdateCron(this);
    }

}
