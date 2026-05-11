package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.Authority;
import com.janwee.bookstore.authorization.core.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpringSecurityUserUnitTest {

    @Test
    void shouldBuildUserDetailsFromDomainMutators() {
        SpringSecurityUser user = new SpringSecurityUser()
                .ofId(42L)
                .withEmail("user@bookstore.com")
                .identifiedBy("secret")
                .ofRole(Role.ADMIN);

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertAll(
                () -> assertEquals(42L, user.id()),
                () -> assertEquals("user@bookstore.com", user.email()),
                () -> assertEquals("user@bookstore.com", user.username()),
                () -> assertEquals("user@bookstore.com", user.getUsername()),
                () -> assertEquals("secret", user.password()),
                () -> assertEquals(List.of(Authority.USER_READ.value(), Authority.USER_WRITE.value()), authorities),
                () -> assertTrue(user.isAccountNonExpired()),
                () -> assertTrue(user.isAccountNonLocked()),
                () -> assertTrue(user.isCredentialsNonExpired()),
                () -> assertTrue(user.isEnabled())
        );
    }

    @Test
    void newUserShouldDefaultToUserRole() {
        SpringSecurityUser user = new SpringSecurityUser()
                .withEmail("user@bookstore.com")
                .identifiedBy("secret");

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertEquals(List.of(Authority.USER_READ.value()), authorities);
    }
}
