package com.janwee.bookstore.authorization.core.presentation.resource;

import com.janwee.bookstore.authorization.core.domain.UserManager;
import com.janwee.bookstore.authorization.core.presentation.message.SigningUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/public")
@Tag(name = "Public Signing Up Resources")
@RestController
@RequiredArgsConstructor
@Validated
public class PublicSigningUpResources {

    private final UserManager userManager;

    @PostMapping("/sign-up")
    @Operation(description = "Register a new user account")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Validated @RequestBody SigningUpRequest request) {
        userManager.createUser(request.toUser());
    }
}
