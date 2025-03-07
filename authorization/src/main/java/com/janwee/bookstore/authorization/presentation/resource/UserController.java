package com.janwee.bookstore.authorization.presentation.resource;

import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.domain.UserManager;
import com.janwee.bookstore.authorization.domain.UserRepository;
import com.janwee.bookstore.authorization.presentation.message.RegisteringUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("users")
@Tag(name = "User Resources")
@RestController
public class UserController {
    private final UserManager userManager;

    private final UserRepository userRepo;

    @Autowired
    public UserController(UserManager userManager, UserRepository userRepo) {
        this.userManager = userManager;
        this.userRepo = userRepo;
    }

    @GetMapping
    @Operation(description = "Retrieve all users")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('READ')")
    public List<User> users() {
        return userRepo.users();
    }

    @GetMapping("/{username}")
    @Operation(description = "Retrieve user of given username")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('READ')")
    public User userWithUsername(@PathVariable String username) {
        return userManager.userOfUsername(username);
    }

    @PostMapping("/sign-in")
    @Operation(description = "Register a new user account")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody RegisteringUserRequest request) {
        userManager.createUser(request.toUser());
    }
}
