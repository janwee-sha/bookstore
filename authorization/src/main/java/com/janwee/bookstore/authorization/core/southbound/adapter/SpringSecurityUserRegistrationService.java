package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.domain.UserRegistrationService;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityUserRegistrationService implements UserRegistrationService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SpringSecurityUserRegistrationService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void add(User user) {
        String encryptedPass = passwordEncoder.encode(user.password());
        user.changePasswordTo(encryptedPass);
        userRepo.save(user);
    }
}
