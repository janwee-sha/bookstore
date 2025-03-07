package com.janwee.bookstore.authorization.infrastructure.security;

import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.domain.UserManager;
import com.janwee.bookstore.authorization.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityBasedUserDetailsManager implements UserManager {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityBasedUserDetailsManager(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userOfUsername(username);
    }

    @Override
    public User userOfUsername(String username) {
        return userRepo.userOfEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " is not found"));
    }

    @Override
    public void createUser(User user) {
        String encryptedPass = passwordEncoder.encode(user.password());
        userRepo.save(user.identifiedBy(encryptedPass));
    }
}
