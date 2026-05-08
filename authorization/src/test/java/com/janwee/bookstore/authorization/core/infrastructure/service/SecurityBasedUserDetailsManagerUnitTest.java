package com.janwee.bookstore.authorization.core.infrastructure.service;

import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.domain.UserRepository;
import com.janwee.bookstore.authorization.core.infrastructure.persistence.SecurityBasedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityBasedUserDetailsManagerUnitTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SecurityBasedUserDetailsManager userDetailsManager;

    @Test
    void shouldLoadUserByUsername() {
        User user = new SecurityBasedUser()
                .withEmail("user@bookstore.com")
                .identifiedBy("encoded-password")
                .ofRole(Role.USER);
        when(userRepo.userOfEmail("user@bookstore.com")).thenReturn(Optional.of(user));

        assertSame(user, userDetailsManager.loadUserByUsername("user@bookstore.com"));
    }

    @Test
    void shouldThrowWhenUsernameDoesNotExist() {
        when(userRepo.userOfEmail("missing@bookstore.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsManager.userOfUsername("missing@bookstore.com"));

        assertEquals("Username missing@bookstore.com is not found", exception.getMessage());
    }

    @Test
    void shouldEncodePasswordBeforeCreatingUser() {
        User user = new SecurityBasedUser()
                .withEmail("new@bookstore.com")
                .identifiedBy("raw-password")
                .ofRole(Role.USER);
        when(passwordEncoder.encode("raw-password")).thenReturn("encoded-password");

        userDetailsManager.createUser(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        assertAll(
                () -> assertEquals("new@bookstore.com", userCaptor.getValue().email()),
                () -> assertEquals("encoded-password", userCaptor.getValue().password())
        );
    }
}
