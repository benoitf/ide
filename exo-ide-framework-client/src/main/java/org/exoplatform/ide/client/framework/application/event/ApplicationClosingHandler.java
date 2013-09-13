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
package org.exoplatform.ide.client.framework.application.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for {@link ApplicationClosingEvent} event.
 *
 * @author <a href="mailto:azatsarynnyy@exoplatform.com">Artem Zatsarynnyy</a>
 * @version $Id: Apr 20, 2012 11:27:00 AM azatsarynnyy $
 */
public interface ApplicationClosingHandler extends EventHandler {
    /**
     * Perform actions, before the browser window closes
     * or navigates to a different site.
     *
     * @param event
     *         the event
     */
    void onApplicationClosing(ApplicationClosingEvent event);
}
