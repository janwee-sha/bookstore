package com.janwee.bookstore.authorization.presentation.resource;

import com.janwee.bookstore.authorization.domain.UserManager;
import com.janwee.bookstore.authorization.presentation.message.RegisteringUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/public/users")
@Tag(name = "Public User Resources")
@RestController
@RequiredArgsConstructor
public class PublicUserResources {

    private final UserManager userManager;

    @PostMapping("/sign-in")
    @Operation(description = "Register a new user account")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody RegisteringUserRequest request) {
        userManager.createUser(request.toUser());
    }
}
