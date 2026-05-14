package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpringSecurityUserServiceUnitTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SpringSecurityUserService userService;

    @Test
    void shouldLoadUserByUsername() {
        User user = new User.Builder()
                .email("user@bookstore.com")
                .password("encoded-password")
                .role(Role.USER)
                .build();
        when(userRepo.userOfEmail("user@bookstore.com")).thenReturn(Optional.of(user));

        assertEquals("user@bookstore.com", userService.loadUserByUsername("user@bookstore.com").getUsername());
    }

    @Test
    void shouldThrowWhenUsernameDoesNotExist() {
        when(userRepo.userOfEmail("missing@bookstore.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("missing@bookstore.com"));

        assertEquals("Username missing@bookstore.com is not found", exception.getMessage());
    }

    @Test
    void shouldEncodePasswordBeforeCreatingUser() {
        User user = new User.Builder()
                .email("new@bookstore.com")
                .password("raw-password")
                .role(Role.USER)
                .build();
        when(passwordEncoder.encode("raw-password")).thenReturn("encoded-password");

        userService.add(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        assertAll(
                () -> assertEquals("new@bookstore.com", userCaptor.getValue().email()),
                () -> assertEquals("encoded-password", userCaptor.getValue().password())
        );
    }
}
