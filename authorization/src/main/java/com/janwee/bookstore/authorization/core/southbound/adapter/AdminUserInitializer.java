package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
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
        userRepo.save(new SpringSecurityUser()
                .withEmail(adminEmail)
                .ofRole(Role.ADMIN)
                .identifiedBy(adminPassword));
    }
}
