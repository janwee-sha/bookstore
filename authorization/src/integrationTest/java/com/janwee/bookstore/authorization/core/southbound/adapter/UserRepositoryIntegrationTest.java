package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.Authority;
import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AuthorizationJpaTestConfiguration.class)
@Import(SpringSecurityUserRepositoryJpaAdapter.class)
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    void shouldSaveAndFindUserByEmail() {
        User user = new SpringSecurityUser()
                .withEmail("user@bookstore.com")
                .identifiedBy("encoded-password")
                .ofRole(Role.ADMIN);

        User saved = userRepo.save(user);
        Optional<SpringSecurityUser> found = Optional.ofNullable(
                (SpringSecurityUser) userRepo.userOfEmail("user@bookstore.com").orElse(null));

        assertAll(
                () -> assertTrue(saved.id() > 0),
                () -> assertTrue(found.isPresent()),
                () -> assertEquals(saved.id(), found.orElseThrow().id()),
                () -> assertEquals("user@bookstore.com", found.orElseThrow().email()),
                () -> assertEquals("encoded-password", found.orElseThrow().password()),
                () -> assertEquals(List.of(Authority.USER_READ.value(), Authority.USER_WRITE.value()),
                        found.orElseThrow().getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList())
        );
    }

    @Test
    void shouldReturnAllPersistedUsers() {
        userRepo.save(new SpringSecurityUser()
                .withEmail("first@bookstore.com")
                .identifiedBy("first-password")
                .ofRole(Role.USER));
        userRepo.save(new SpringSecurityUser()
                .withEmail("second@bookstore.com")
                .identifiedBy("second-password")
                .ofRole(Role.USER));

        List<String> emails = userRepo.users().stream()
                .map(User::email)
                .sorted()
                .toList();

        assertEquals(List.of("first@bookstore.com", "second@bookstore.com"), emails);
    }
}
