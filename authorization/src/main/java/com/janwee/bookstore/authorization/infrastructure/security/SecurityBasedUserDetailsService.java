package com.janwee.bookstore.authorization.infrastructure.security;

import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.domain.UserRepository;
import com.janwee.bookstore.authorization.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityBasedUserDetailsService implements UserService {
    private final UserRepository userRepo;

    @Autowired
    public SecurityBasedUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
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
}
