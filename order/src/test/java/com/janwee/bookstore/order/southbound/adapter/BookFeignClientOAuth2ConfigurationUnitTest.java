package com.janwee.bookstore.order.southbound.adapter;

import com.janwee.bookstore.order.southbound.adapter.service.BookFeignClientOAuth2Configuration;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookFeignClientOAuth2ConfigurationUnitTest {

    @Test
    void shouldAddBearerTokenHeaderWhenOAuth2ClientIsAuthorized() {
        OAuth2AuthorizedClientManager authorizedClientManager = mock(OAuth2AuthorizedClientManager.class);
        when(authorizedClientManager.authorize(any()))
                .thenReturn(authorizedClient("access-token"));

        RequestInterceptor interceptor = new BookFeignClientOAuth2Configuration()
                .bookFeignClientOAuth2RequestInterceptor(authorizedClientManager);
        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertEquals("Bearer access-token", template.headers().get(HttpHeaders.AUTHORIZATION).iterator().next());

        ArgumentCaptor<OAuth2AuthorizeRequest> authorizeRequestCaptor =
                ArgumentCaptor.forClass(OAuth2AuthorizeRequest.class);
        verify(authorizedClientManager).authorize(authorizeRequestCaptor.capture());
        assertEquals("order-service", authorizeRequestCaptor.getValue().getClientRegistrationId());
        assertEquals("order-service", authorizeRequestCaptor.getValue().getPrincipal().getName());
    }

    @Test
    void shouldThrowExceptionWhenOAuth2ClientCannotBeAuthorized() {
        OAuth2AuthorizedClientManager authorizedClientManager = mock(OAuth2AuthorizedClientManager.class);
        when(authorizedClientManager.authorize(any())).thenReturn(null);

        RequestInterceptor interceptor = new BookFeignClientOAuth2Configuration()
                .bookFeignClientOAuth2RequestInterceptor(authorizedClientManager);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> interceptor.apply(new RequestTemplate()));
        assertEquals("Failed to authorize OAuth2 client: order-service", exception.getMessage());
    }

    private OAuth2AuthorizedClient authorizedClient(String tokenValue) {
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId("order-service")
                .clientId("order-service")
                .clientSecret("secret")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri("http://localhost/oauth2/token")
                .build();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                tokenValue,
                Instant.now(),
                Instant.now().plusSeconds(60));
        return new OAuth2AuthorizedClient(clientRegistration, "order-service", accessToken);
    }
}
