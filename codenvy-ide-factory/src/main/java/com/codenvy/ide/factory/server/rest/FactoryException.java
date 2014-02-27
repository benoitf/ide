package com.codenvy.ide.factory.server.rest;

/**
 * @author vzhukovskii@codenvy.com
 */
public class FactoryException extends Exception {
    private int status = 400;

    public FactoryException() {
        super();
    }

    public FactoryException(String message) {
        super(message);
    }

    public FactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public FactoryException(int status, String message) {
        super(message);
        this.status = status;
    }

    public FactoryException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
