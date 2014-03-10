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
package org.exoplatform.ide.extension.java.jdi.server;

/**
 * Thrown by {@link ApplicationRunner}.
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
@SuppressWarnings("serial")
public class ApplicationRunnerException extends Exception {
    private final String logs;

    public ApplicationRunnerException(String message, Throwable cause, String logs) {
        super(message, cause);
        this.logs = logs;
    }

    public ApplicationRunnerException(String message, Throwable cause) {
        super(message, cause);
        this.logs = null;
    }

    public ApplicationRunnerException(String message) {
        super(message);
        this.logs = null;
    }

    public String getLogs() {
        return logs;
    }
}