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
package org.exoplatform.ide.extension.java.jdi.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for {@link ShowBreakpointPropertiesEvent} event.
 *
 * @author <a href="mailto:azatsarynnyy@exoplatform.org">Artem Zatsarynnyy</a>
 * @version $Id: ShowBreakpointPropertiesHandler.java May 8, 2012 13:00:25 PM azatsarynnyy $
 */
public interface ShowBreakpointPropertiesHandler extends EventHandler {
    /**
     * @param event
     *         {@link ShowBreakpointPropertiesEvent}
     */
    void onShowBreakpointProperties(ShowBreakpointPropertiesEvent e);
}
