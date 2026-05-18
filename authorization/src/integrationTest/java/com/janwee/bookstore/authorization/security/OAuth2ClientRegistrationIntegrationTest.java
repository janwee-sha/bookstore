package com.janwee.bookstore.authorization.security;

import com.janwee.bookstore.authorization.AuthorizationApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = AuthorizationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class OAuth2ClientRegistrationIntegrationTest {
    @Autowired
    private RegisteredClientRepository registeredClientRepo;

    @Test
    void shouldRegisterBookstoreFrontendClient() {
        RegisteredClient client = registeredClientRepo.findByClientId("bookstore-frontend");

        assertNotNull(client);
        assertAll(
                () -> assertTrue(client.getClientAuthenticationMethods().contains(ClientAuthenticationMethod.NONE)),
                () -> assertTrue(client.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE)),
                () -> assertTrue(client.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)),
                () -> assertTrue(client.getRedirectUris().contains("http://127.0.0.1:8088/authorized")),
                () -> assertTrue(client.getClientSettings().isRequireProofKey())
        );
    }

    @Test
    void shouldRegisterOrderServiceClient() {
        RegisteredClient client = registeredClientRepo.findByClientId("order-service");

        assertNotNull(client);
        assertAll(
                () -> assertEquals("order-service", client.getClientId()),
                () -> assertTrue(client.getClientAuthenticationMethods().contains(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)),
                () -> assertTrue(client.getAuthorizationGrantTypes().contains(AuthorizationGrantType.CLIENT_CREDENTIALS)),
                () -> assertTrue(client.getScopes().contains("book:read"))
        );
    }
}
