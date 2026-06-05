package com.janwee.bookstore.authorization.core.northbound.remote.rest;

import com.janwee.bookstore.authorization.core.domain.Role;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserResourceSecurityIntegrationTest extends RestApiIntegrationTestSupport {

    @Test
    void usersShouldAllowOnlyUserReadWriteAccess() throws Exception {
        saveUser();

        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/users").with(userReader()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users").with(userWriter()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users").with(orderWriter()))
                .andExpect(status().isForbidden());
    }

    @Test
    void userOfUsernameShouldAllowOnlyUserReadWriteAccess() throws Exception {
        saveUser();

        mockMvc.perform(get("/users/{username}", "reader@bookstore.com"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/users/{username}", "reader@bookstore.com").with(userReader()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users/{username}", "reader@bookstore.com").with(userWriter()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users/{username}", "reader@bookstore.com").with(orderWriter()))
                .andExpect(status().isForbidden());
    }

    private void saveUser() {
        saveUser("reader@bookstore.com", "P@ssw0rd!", Role.USER);
    }
}
