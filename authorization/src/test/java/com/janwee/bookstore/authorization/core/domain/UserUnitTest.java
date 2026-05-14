package com.janwee.bookstore.authorization.core.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserUnitTest {

    @Test
    void shouldBuildUserFromDomainMutators() {
        User user = new User.Builder()
                .id(42L)
                .email("user@bookstore.com")
                .password("secret")
                .role(Role.ADMIN)
                .build();

        assertAll(
                () -> assertEquals(42L, user.id()),
                () -> assertEquals("user@bookstore.com", user.email()),
                () -> assertEquals("user@bookstore.com", user.username()),
                () -> assertEquals("secret", user.password()),
                () -> assertEquals(Role.ADMIN, user.role()),
                () -> assertEquals(List.of(Authority.USER_READ, Authority.USER_WRITE), user.authorities())
        );
    }

    @Test
    void newUserShouldDefaultToUserRole() {
        User user = new User.Builder()
                .email("user@bookstore.com")
                .password("secret")
                .build();

        assertAll(
                () -> assertEquals(Role.USER, user.role()),
                () -> assertEquals(List.of(Authority.USER_READ), user.authorities())
        );
    }

    @Test
    void shouldChangePassword() {
        User user = new User.Builder().password("old-password").build();

        user.changePasswordTo("new-password");

        assertEquals("new-password", user.password());
    }
}
