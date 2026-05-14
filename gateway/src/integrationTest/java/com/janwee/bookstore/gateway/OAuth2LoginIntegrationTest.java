package com.janwee.bookstore.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ClassUtils;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = GatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = {
                "spring.config.import=",
                "spring.cloud.config.enabled=false",
                "spring.cloud.discovery.enabled=false",
                "eureka.client.enabled=false",
                "spring.security.oauth2.client.registration.github.client-id=test-github-client",
                "spring.security.oauth2.client.registration.github.client-secret=test-github-secret",
                "spring.security.oauth2.client.registration.bookstore.client-secret=test-bookstore-secret",
                "spring.security.oauth2.client.registration.bookstore.redirect-uri=http://127.0.0.1:7001/authorized",
                "spring.security.oauth2.client.provider.spring.authorization-uri=http://localhost:7030/oauth2/authorize",
                "spring.security.oauth2.client.provider.spring.token-uri=http://localhost:7030/oauth2/token",
                "spring.security.oauth2.client.provider.spring.jwk-set-uri=http://localhost:7030/oauth2/jwks"
        }
)
@AutoConfigureMockMvc
class OAuth2LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Test
    void shouldRedirectBookstoreLoginToAuthorizationServerWithConfiguredCallback() throws Exception {
        String location = mockMvc.perform(get("/oauth2/authorization/bookstore"))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader(HttpHeaders.LOCATION);

        assertNotNull(location);
        URI uri = URI.create(location);
        Map<String, List<String>> query = queryParameters(uri);

        assertAll(
                () -> assertEquals("http", uri.getScheme()),
                () -> assertEquals("localhost", uri.getHost()),
                () -> assertEquals(7030, uri.getPort()),
                () -> assertEquals("/oauth2/authorize", uri.getPath()),
                () -> assertEquals("code", singleValue(query, "response_type")),
                () -> assertEquals("bookstore", singleValue(query, "client_id")),
                () -> assertEquals("http://127.0.0.1:7001/authorized", singleValue(query, "redirect_uri")),
                () -> assertTrue(singleValue(query, "scope").contains("openid"))
        );
    }

    @Test
    void shouldHandleConfiguredAuthorizationCallbackThroughOAuth2LoginFilter() throws Exception {
        mockMvc.perform(get("/authorized")
                        .param("code", "test-code")
                        .param("state", "missing-state"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void shouldConfigureOidcClientWithJoseRuntimeSupport() {
        ClientRegistration bookstore = clientRegistrationRepository.findByRegistrationId("bookstore");

        assertAll(
                () -> assertNotNull(bookstore),
                () -> assertTrue(bookstore.getScopes().contains("openid")),
                () -> assertEquals("http://localhost:7030/oauth2/jwks",
                        bookstore.getProviderDetails().getJwkSetUri()),
                () -> assertTrue(ClassUtils.isPresent(
                        "org.springframework.security.oauth2.jwt.JwtDecoder",
                        getClass().getClassLoader()))
        );
    }

    private static Map<String, List<String>> queryParameters(URI uri) {
        return Arrays.stream(uri.getRawQuery().split("&"))
                .map(parameter -> parameter.split("=", 2))
                .collect(Collectors.toMap(
                        parameter -> decode(parameter[0]),
                        parameter -> List.of(decode(parameter.length > 1 ? parameter[1] : "")),
                        (left, right) -> {
                            List<String> values = new ArrayList<>(left);
                            values.addAll(right);
                            return values;
                        }
                ));
    }

    private static String singleValue(Map<String, List<String>> query, String name) {
        List<String> values = query.get(name);
        assertNotNull(values, "Missing query parameter: " + name);
        assertEquals(1, values.size(), "Expected one query parameter value for: " + name);
        return values.get(0);
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
