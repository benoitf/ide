package com.codenvy.ide.factory.server;

import com.codenvy.api.factory.dto.AdvancedFactoryUrl;
import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.factory.server.rest.FactoryException;
import com.codenvy.ide.factory.server.rest.FactoryService;

import org.everrest.assured.EverrestJetty;
import org.everrest.assured.JettyHttpServer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;


import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * @author vzhukovskii@codenvy.com
 */
@Listeners(value = {EverrestJetty.class, MockitoTestNGListener.class})
public class FactoryServiceTest {
    @Mock
    private Factory factory;

    @InjectMocks
    private FactoryService factoryService;

    @Test
    public void shouldSendEmailWithFactory() throws Exception {
        doNothing().when(factory).sendFactory("user@example.com", "message content");

        given().auth().basic(JettyHttpServer.ADMIN_USER_NAME, JettyHttpServer.ADMIN_USER_PASSWORD).pathParam("ws-name", "test")
                .formParam("email", "user@example.com").formParam("message", "message content").expect().statusCode(204).when()
                .post("/private/factory/{ws-name}/send");
    }

    @Test
    public void shouldThrowMessagingExceptionWhenTrySendEmailWithFactory() throws Exception {
        doThrow(new FactoryException("Failed to send mail")).when(factory).sendFactory("user@example.com", "message content");

        given().auth().basic(JettyHttpServer.ADMIN_USER_NAME, JettyHttpServer.ADMIN_USER_PASSWORD).pathParam("ws-name", "test")
                .formParam("email", "user@example.com").formParam("message", "message content").expect().statusCode(500)
                .body(equalTo("Failed to send mail")).when().post("/private/factory/{ws-name}/send");
    }

    @Test
    public void shouldNotSendEmailWithFactoryForUnauthorizedUser() throws Exception {
        given().pathParam("ws-name", "test").expect().statusCode(401).when().get("/private/factory/{ws-name}/send");
    }

    @Test
    public void shouldAcceptFactory() throws Exception {
        AdvancedFactoryUrl factory = DtoFactory.getInstance().createDto(AdvancedFactoryUrl.class);

        doNothing().when(this.factory).acceptFactory((AdvancedFactoryUrl)any());
        given().auth().basic(JettyHttpServer.ADMIN_USER_NAME, JettyHttpServer.ADMIN_USER_PASSWORD).pathParam("ws-name", "test")
                .header("Content-Type", MediaType.APPLICATION_JSON).content(DtoFactory.getInstance().toJson(factory))
                .expect().statusCode(204).when().post("/private/factory/{ws-name}/accept");
    }

    @Test
    public void shouldThrowExceptionWhenAcceptFactory() throws Exception {
        AdvancedFactoryUrl factory = DtoFactory.getInstance().createDto(AdvancedFactoryUrl.class);

        doThrow(new FactoryException("message")).when(this.factory).acceptFactory((AdvancedFactoryUrl)any());
        given().auth().basic(JettyHttpServer.ADMIN_USER_NAME, JettyHttpServer.ADMIN_USER_PASSWORD).pathParam("ws-name", "test")
                .header("Content-Type", MediaType.APPLICATION_JSON).content(DtoFactory.getInstance().toJson(factory))
                .expect().statusCode(500).body(equalTo("message")).when().post("/private/factory/{ws-name}/accept");
    }

    @Test
    public void shouldNotAcceptFactoryForUnauthorizedUser() throws Exception {
        given().pathParam("ws-name", "test").expect().statusCode(401).when().get("/private/factory/{ws-name}/accept");
    }
}
