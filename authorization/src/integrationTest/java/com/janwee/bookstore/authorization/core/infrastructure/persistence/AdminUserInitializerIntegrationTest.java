package com.janwee.bookstore.authorization.core.infrastructure.persistence;

import com.janwee.bookstore.authorization.core.domain.Authority;
import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.domain.UserRepository;
import com.janwee.bookstore.authorization.core.infrastructure.persistence.jpa.SecurityBasedUserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AuthorizationJpaTestConfiguration.class)
@Import({AdminUserInitializer.class, SecurityBasedUserRepositoryImpl.class})
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
    private SecurityBasedUserJpaRepository userJpaRepo;

    @BeforeEach
    void cleanDatabase() {
        userJpaRepo.deleteAllInBatch();
        userJpaRepo.flush();
    }

    @Test
    void shouldCreateAdminUserWhenMissing() {
        adminUserInitializer.run(new DefaultApplicationArguments());

        User admin = userRepo.userOfEmail("admin@bookstore.com").orElseThrow();
        List<String> authorities = admin.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertAll(
                () -> assertTrue(admin.id() > 0),
                () -> assertEquals("admin@bookstore.com", admin.email()),
                () -> assertEquals("{noop}password_1", admin.password()),
                () -> assertEquals(List.of(Authority.USER_READ.value(), Authority.USER_WRITE.value()), authorities)
        );
    }

    @Test
    void shouldLeaveExistingAdminUserUntouched() {
        userRepo.save(new SecurityBasedUser()
                .withEmail("admin@bookstore.com")
                .identifiedBy("password_2")
                .ofRole(Role.USER));

        adminUserInitializer.run(new DefaultApplicationArguments());

        List<User> users = userRepo.users();
        User admin = userRepo.userOfEmail("admin@bookstore.com").orElseThrow();
        List<String> authorities = admin.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertAll(
                () -> assertEquals(1, users.size()),
                () -> assertEquals("password_2", admin.password()),
                () -> assertEquals(List.of(Authority.USER_READ.value()), authorities)
        );
    }
}
