package com.janwee.bookstore.authorization.core.presentation.resource;

import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.domain.UserManager;
import com.janwee.bookstore.authorization.core.domain.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@Tag(name = "User Resources")
@RestController
@RequiredArgsConstructor
public class UserResources {
    private final UserManager userManager;

    private final UserRepository userRepo;

    @GetMapping
    @Operation(description = "Retrieve all users")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('user:read')")
    public List<User> users() {
        return userRepo.users();
    }

    @GetMapping("/{username}")
    @Operation(description = "Retrieve user of given username")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('user:read')")
    public User userWithUsername(@PathVariable String username) {
        return userManager.userOfUsername(username);
    }
}
