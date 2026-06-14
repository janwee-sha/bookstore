package com.janwee.bookstore.authorization.southbound.adapter;

import com.janwee.bookstore.authorization.domain.Authority;
import com.janwee.bookstore.authorization.domain.Role;
import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.southbound.adapter.jpa.SpringSecurityUserJpaRepository;
import com.janwee.bookstore.authorization.southbound.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AuthorizationJpaTestConfiguration.class)
@Import({AdminUserInitializer.class, SpringSecurityUserRepositoryJpaAdapter.class})
@TestPropertySource(properties = {
        "spring.security.user.name=admin@bookstore.com",
        "spring.security.user.password={noop}password_1"
})
class AdminUserInitializerIntegrationTest {

    @Autowired
    private AdminUserInitializer adminUserInitializer;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SpringSecurityUserJpaRepository userJpaRepo;

    @BeforeEach
    void cleanDatabase() {
        userJpaRepo.deleteAllInBatch();
        userJpaRepo.flush();
    }

    @Test
    void shouldCreateAdminUserWhenMissing() {
        adminUserInitializer.run(new DefaultApplicationArguments());

        User admin = userRepo.userOfEmail("admin@bookstore.com").orElseThrow();

        assertAll(
                () -> assertTrue(admin.id() > 0),
                () -> assertEquals("admin@bookstore.com", admin.email()),
                () -> assertEquals("{noop}password_1", admin.password()),
                () -> assertEquals(List.of(Authority.USER_READ, Authority.USER_WRITE), admin.authorities())
        );
    }

    @Test
    void shouldLeaveExistingAdminUserUntouched() {
        userRepo.save(new User.Builder()
                .email("admin@bookstore.com")
                .password("password_2")
                .role(Role.USER)
                .build());

        adminUserInitializer.run(new DefaultApplicationArguments());

        List<User> users = userRepo.users();
        User admin = userRepo.userOfEmail("admin@bookstore.com").orElseThrow();

        assertAll(
                () -> assertEquals(1, users.size()),
                () -> assertEquals("password_2", admin.password()),
                () -> assertEquals(List.of(Authority.USER_READ), admin.authorities())
        );
    }
}
