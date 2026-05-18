package com.janwee.bookstore.order.core.southbound.adapter;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

public class BookFeignClientOAuth2Configuration {
    private static final String CLIENT_REGISTRATION_ID = "order-service";
    private static final Authentication ORDER_SERVICE_PRINCIPAL =
            new AnonymousAuthenticationToken(
                    "order-service",
                    "order-service",
                    AuthorityUtils.createAuthorityList("ROLE_SYSTEM"));

    @Bean
    public RequestInterceptor bookFeignClientOAuth2RequestInterceptor(
            OAuth2AuthorizedClientManager authorizedClientManager) {
        return template -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(CLIENT_REGISTRATION_ID)
                    .principal(ORDER_SERVICE_PRINCIPAL)
                    .build();
            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
            if (authorizedClient == null) {
                throw new IllegalStateException("Failed to authorize OAuth2 client: " + CLIENT_REGISTRATION_ID);
            }
            template.header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + authorizedClient.getAccessToken().getTokenValue());
        };
    }
}
