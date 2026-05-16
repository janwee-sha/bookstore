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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpringSecurityUserRegistrationServiceUnitTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private SpringSecurityUserRegistrationService userRegService;

    @Test
    void shouldEncodePasswordBeforeCreatingUser() {
        User user = new User.Builder()
                .email("new@bookstore.com")
                .password("raw-password")
                .role(Role.USER)
                .build();
        when(passwordEncoder.encode("raw-password")).thenReturn("encoded-password");

        userRegService.add(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        assertAll(
                () -> assertEquals("new@bookstore.com", userCaptor.getValue().email()),
                () -> assertEquals("encoded-password", userCaptor.getValue().password())
        );
    }
}
