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
package org.exoplatform.ide.extension.cloudbees.client.project;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event occurs, when user tries to manage project, deployed to CloudBees.
 *
 * @author <a href="mailto:azhuleva@exoplatform.com">Ann Shumilova</a>
 * @version $Id: Dec 5, 2011 12:34:34 PM anya $
 */
public class ManageCloudBeesProjectEvent extends GwtEvent<ManageCloudBeesProjectHandler> {
    /** Type used to register the event. */
    public static final GwtEvent.Type<ManageCloudBeesProjectHandler> TYPE =
            new GwtEvent.Type<ManageCloudBeesProjectHandler>();

    /** @see com.google.gwt.event.shared.GwtEvent#getAssociatedType() */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ManageCloudBeesProjectHandler> getAssociatedType() {
        return TYPE;
    }

    /** @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler) */
    @Override
    protected void dispatch(ManageCloudBeesProjectHandler handler) {
        handler.onManageCloudBeesProject(this);
    }

}