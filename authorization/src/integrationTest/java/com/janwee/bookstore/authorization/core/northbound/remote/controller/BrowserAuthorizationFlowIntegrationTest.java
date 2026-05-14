package com.janwee.bookstore.authorization.core.northbound.remote.controller;

import com.janwee.bookstore.authorization.AuthorizationApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = AuthorizationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
public class BrowserAuthorizationFlowIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderLoginPageForBrowserAuthorizationFlow() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
}
