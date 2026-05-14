package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.Authority;
import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpringSecurityUserUnitTest {

    @Test
    void shouldAdaptDomainUserToUserDetails() {
        User domainUser = new User.Builder()
                .id(42L)
                .email("user@bookstore.com")
                .password("secret")
                .role(Role.ADMIN)
                .build();

        SpringSecurityUser user = SpringSecurityUserRepositoryJpaAdapter.toSpringSecurityUser(domainUser);
        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertAll(
                () -> assertEquals(42L, user.getId()),
                () -> assertEquals("user@bookstore.com", user.getEmail()),
                () -> assertEquals("user@bookstore.com", user.getUsername()),
                () -> assertEquals("secret", user.getPassword()),
                () -> assertEquals(List.of(Authority.USER_READ.value(), Authority.USER_WRITE.value()), authorities),
                () -> assertTrue(user.isAccountNonExpired()),
                () -> assertTrue(user.isAccountNonLocked()),
                () -> assertTrue(user.isCredentialsNonExpired()),
                () -> assertTrue(user.isEnabled())
        );
    }

    @Test
    void newUserShouldDefaultToUserRole() {
        SpringSecurityUser user = SpringSecurityUserRepositoryJpaAdapter.toSpringSecurityUser(
                new User.Builder()
                        .email("user@bookstore.com")
                        .password("secret")
                        .build());

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertEquals(List.of(Authority.USER_READ.value()), authorities);
    }
}
