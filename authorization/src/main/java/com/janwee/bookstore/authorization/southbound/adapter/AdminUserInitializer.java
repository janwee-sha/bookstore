package com.janwee.bookstore.authorization.southbound.adapter;

import com.janwee.bookstore.authorization.domain.Role;
import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.southbound.port.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements ApplicationRunner {
    private final UserRepository userRepo;
    private final String adminEmail;
    private final String adminPassword;

    public AdminUserInitializer(UserRepository userRepo,
                                @Value("${spring.security.user.name}") String adminEmail,
                                @Value("${spring.security.user.password}") String adminPassword) {
        this.userRepo = userRepo;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepo.userOfEmail(adminEmail).isPresent()) {
            return;
        }
        userRepo.save(new User.Builder()
                .email(adminEmail)
                .role(Role.ADMIN)
                .password(adminPassword)
                .build());
    }
}
