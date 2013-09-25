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
package com.codenvy.ide.ext.aws.shared.beanstalk;

/**
 * Limits the events returned for {@link ListEventsRequest}. Only events with specified severity or higher will be
 * returned.
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public enum EventsSeverity {
    TRACE("TRACE"), DEBUG("DEBUG"), INFO("INFO"), WARN("WARN"), ERROR("ERROR"), FATAL("FATAL");

    private final String value;

    private EventsSeverity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Use this in place of valueOf.
     *
     * @param value
     *         real value
     * @return EventsSeverity corresponding to the value
     */
    public static EventsSeverity fromValue(String value) {
        for (EventsSeverity v : EventsSeverity.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Invalid value '" + value + "' ");
    }
}
