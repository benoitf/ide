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
package com.codenvy.ide.ext.java.jdi.server;

/**
 * Thrown if connection to JVM is not established or lost.
 *
 * @author andrew00x
 */
@SuppressWarnings("serial")
public final class VMConnectException extends DebuggerException {
    public VMConnectException(String message) {
        super(message);
    }

    public VMConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
