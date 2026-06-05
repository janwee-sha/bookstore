package com.janwee.bookstore.authorization.core.northbound.remote.rest;

import com.janwee.bookstore.authorization.core.domain.Role;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserResourceCoreIntegrationTest extends RestApiIntegrationTestSupport {

    @Test
    void shouldReturnUsers() throws Exception {
        saveUser("reader@bookstore.com", "P@ssw0rd!", Role.USER);
        saveUser("admin@bookstore.com", "P@ssw0rd!", Role.ADMIN);

        mockMvc.perform(get("/users").with(userReader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder(
                        "reader@bookstore.com",
                        "admin@bookstore.com"
                )))
                .andExpect(jsonPath("$[?(@.email == 'reader@bookstore.com')].role").value("USER"))
                .andExpect(jsonPath("$[?(@.email == 'admin@bookstore.com')].role").value("ADMIN"))
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }

    @Test
    void shouldReturnUserOfUsername() throws Exception {
        saveUser("reader@bookstore.com", "P@ssw0rd!", Role.USER);

        mockMvc.perform(get("/users/{username}", "reader@bookstore.com").with(userReader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("reader@bookstore.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void shouldReturnNotFoundForMissingUser() throws Exception {
        mockMvc.perform(get("/users/{username}", "missing@bookstore.com").with(userReader()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("No such user of username: missing@bookstore.com"))
                .andExpect(jsonPath("$.path").value("/users/missing@bookstore.com"));
    }
}
