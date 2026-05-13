package com.janwee.bookstore.authorization.core.northbound.remote.rest;

import com.janwee.bookstore.authorization.AuthorizationApplication;
import com.janwee.bookstore.authorization.core.southbound.adapter.jpa.SpringSecurityUserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
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
class UserResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringSecurityUserJpaRepository userJpaRepo;

    @BeforeEach
    void cleanDatabase() {
        userJpaRepo.deleteAll();
    }

    @Test
    void shouldRequireAuthenticationForUsersEndpoint() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection());
    }
}
