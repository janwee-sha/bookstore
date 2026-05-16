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
class FrontendClientRegistrationIntegrationTest {
    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Test
    void shouldRegisterPublicPkceClientForReactFrontend() {
        RegisteredClient client = registeredClientRepository.findByClientId("bookstore-frontend");

        assertNotNull(client);
        assertAll(
                () -> assertTrue(client.getClientAuthenticationMethods().contains(ClientAuthenticationMethod.NONE)),
                () -> assertTrue(client.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE)),
                () -> assertTrue(client.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)),
                () -> assertTrue(client.getRedirectUris().contains("http://127.0.0.1:8088/authorized")),
                () -> assertTrue(client.getClientSettings().isRequireProofKey())
        );
    }
}
