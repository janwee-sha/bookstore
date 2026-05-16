package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatasourceUserDetailsServiceUnitTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private DatasourceUserDetailsService userDetailsService;

    @Test
    void shouldLoadUserByUsername() {
        User user = new User.Builder()
                .email("user@bookstore.com")
                .password("encoded-password")
                .role(Role.USER)
                .build();
        when(userRepo.userOfEmail("user@bookstore.com")).thenReturn(Optional.of(user));

        assertEquals("user@bookstore.com", userDetailsService.loadUserByUsername("user@bookstore.com").getUsername());
    }

    @Test
    void shouldThrowWhenUsernameDoesNotExist() {
        when(userRepo.userOfEmail("missing@bookstore.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing@bookstore.com"));

        assertEquals("Username missing@bookstore.com is not found", exception.getMessage());
    }
}
