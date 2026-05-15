package com.janwee.bookstore.authorization.core.northbound.remote.controller;

import com.janwee.bookstore.authorization.AuthorizationApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = AuthorizationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
public class BrowserAuthorizationFlowIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestCache requestCache;

    @Test
    void shouldRenderLoginPageForBrowserAuthorizationFlow() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotCacheChromeDevtoolsWellKnownProbeAsBrowserAuthorizationFlow() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/.well-known/appspecific/com.chrome.devtools.json")
                        .param("continue", "")
                        .accept(MediaType.TEXT_HTML)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        assertNull(session.getAttribute("SPRING_SECURITY_SAVED_REQUEST"));
    }

    @Test
    void requestCacheShouldOnlyStoreAuthorizationEndpointRequests() {
        MockHttpServletRequest devtoolsRequest = new MockHttpServletRequest("GET",
                "/.well-known/appspecific/com.chrome.devtools.json");
        devtoolsRequest.setServletPath("/.well-known/appspecific/com.chrome.devtools.json");
        MockHttpServletResponse devtoolsResponse = new MockHttpServletResponse();

        requestCache.saveRequest(devtoolsRequest, devtoolsResponse);

        assertNull(devtoolsRequest.getSession(false));

        MockHttpServletRequest authorizationRequest = new MockHttpServletRequest("GET", "/oauth2/authorize");
        authorizationRequest.setServletPath("/oauth2/authorize");
        MockHttpServletResponse authorizationResponse = new MockHttpServletResponse();

        requestCache.saveRequest(authorizationRequest, authorizationResponse);

        assertTrue(requestCache.getRequest(authorizationRequest, authorizationResponse)
                .getRedirectUrl()
                .startsWith("http://localhost/oauth2/authorize"));
    }
}
