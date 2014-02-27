package com.codenvy.ide.factory.server.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author vzhukovskii@codenvy.com
 */
@Provider
public class FactoryExceptionMapper implements ExceptionMapper<FactoryException> {
    @Override
    public Response toResponse(FactoryException exception) {
        return Response.status(exception.getStatus()).header("JAXRS-Body-Provided", "Error-Message").entity(exception.getMessage())
                       .type(MediaType.TEXT_PLAIN_TYPE).build();
    }
}
