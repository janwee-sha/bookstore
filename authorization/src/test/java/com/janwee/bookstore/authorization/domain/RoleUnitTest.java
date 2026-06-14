package com.janwee.bookstore.authorization.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleUnitTest {

    @Test
    void adminShouldHaveReadAndWriteAuthorities() {
        assertEquals(List.of(Authority.USER_READ, Authority.USER_WRITE), Role.ADMIN.authorities());
    }

    @Test
    void userShouldHaveReadAuthorityOnly() {
        assertEquals(List.of(Authority.USER_READ), Role.USER.authorities());
    }

    @Test
    void roleAuthoritiesShouldBeImmutable() {
        assertThrows(UnsupportedOperationException.class,
                () -> Role.USER.authorities().add(Authority.USER_WRITE));
    }
}
