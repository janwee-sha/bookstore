package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.domain.UserService;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityUserService implements UserService, UserDetailsService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SpringSecurityUserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userOf(username);
    }

    @Override
    public User userOf(String username) {
        return userRepo.userOfEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " is not found"));
    }

    @Override
    public void add(User user) {
        String encryptedPass = passwordEncoder.encode(user.password());
        user.changePasswordTo(encryptedPass);
        userRepo.save(user);
    }
}
