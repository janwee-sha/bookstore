package com.janwee.bookstore.authorization.core.domain;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleUnitTest {

    @Test
    void adminShouldHaveReadAndWriteAuthorities() {
        List<String> authorities = Role.ADMIN.authorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertEquals(List.of(Authority.USER_READ.value(), Authority.USER_WRITE.value()), authorities);
    }

    @Test
    void userShouldHaveReadAuthorityOnly() {
        List<String> authorities = Role.USER.authorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertEquals(List.of(Authority.USER_READ.value()), authorities);
    }

    @Test
    void roleAuthoritiesShouldBeImmutable() {
        assertThrows(UnsupportedOperationException.class,
                () -> Role.USER.authorities().add(Authority.USER_WRITE::value));
    }
}
