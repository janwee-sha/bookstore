package com.janwee.bookstore.authorization.presentation.resource;

import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.domain.UserManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("users")
@Tag(name = "User Resources")
@RestController
public class UserController {
    private final UserManager userManager;

    @Autowired
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping("/{username}")
    @Operation(description = "Retrieve user of given username")
    @ResponseStatus(HttpStatus.OK)
    public User userWithUsername(@PathVariable String username) {
        return userManager.userOfUsername(username);
    }
}
