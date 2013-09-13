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
package org.exoplatform.ide.extension.cloudfoundry.server;

/**
 * If Cloudfoundry server return unexpected or error status for request.
 *
 * @author <a href="mailto:aparfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: $
 */
@SuppressWarnings("serial")
public class CloudfoundryException extends Exception {
    /** HTTP status of response from Cloudfoundry server. */
    private final int responseStatus;

    /** Content type of response from Cloudfoundry server. */
    private final String contentType;

    /**
     * Exit code of command execution at Cloudfoundry server. May be -1 if cannot get exit code from Cloudfoundry
     * response.
     */
    private final int exitCode;

    /**
     * @param responseStatus
     *         HTTP status of response from Cloudfoundry server
     * @param message
     *         text message
     * @param contentType
     *         content type of response from Cloudfoundry server
     */
    public CloudfoundryException(int responseStatus, String message, String contentType) {
        this(responseStatus, -1, message, contentType);
    }

    public CloudfoundryException(int responseStatus, int exitCode, String message, String contentType) {
        super(message);
        this.responseStatus = responseStatus;
        this.exitCode = exitCode;
        this.contentType = contentType;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public String getContentType() {
        return contentType;
    }

    public int getExitCode() {
        return exitCode;
    }

    @Override
    public String toString() {
        return "CloudfoundryException{" +
               "responseStatus=" + responseStatus +
               ", message='" + getMessage() + '\'' +
               ", contentType='" + contentType + '\'' +
               ", exitCode=" + exitCode +
               '}';
    }
}
