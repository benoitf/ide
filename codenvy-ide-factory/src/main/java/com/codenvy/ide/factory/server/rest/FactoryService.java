package com.codenvy.ide.factory.server.rest;

import com.codenvy.api.factory.dto.AdvancedFactoryUrl;
import com.codenvy.ide.factory.server.Factory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author vzhukovskii@codenvy.com
 */
@Path("/factory")
public class FactoryService extends com.codenvy.api.factory.FactoryService {
    @Inject
    private Factory factory;

    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("developer")
    public void sendFactoryUrlToEmail(@FormParam("email") String email, @FormParam("message") String message) throws FactoryException {
        factory.sendFactory(email, message);
    }

    @POST
    @Path("accept")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"developer"})
    public void acceptFactory(final AdvancedFactoryUrl factory) throws FactoryException {
        this.factory.acceptFactory(factory);
        this.factory.setPrivateWorkspaceAttr(factory);
    }
}
