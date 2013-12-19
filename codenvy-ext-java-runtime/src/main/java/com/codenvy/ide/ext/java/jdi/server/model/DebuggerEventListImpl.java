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
package com.codenvy.ide.ext.java.jdi.server.model;



import com.codenvy.ide.ext.java.jdi.shared.DebuggerEvent;
import com.codenvy.ide.ext.java.jdi.shared.DebuggerEventList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class DebuggerEventListImpl implements DebuggerEventList {
    private List<DebuggerEvent> events;

    public DebuggerEventListImpl(List<DebuggerEvent> events) {
        this.events = events;
    }

    @Override
    public List<DebuggerEvent> getEvents() {
        if (events == null) {
            events = new ArrayList<DebuggerEvent>();
        }
        return events;
    }


    @Override
    public void setEvents(List<DebuggerEvent> events) {
        this.events = events;
    }
}