package com.janwee.bookstore.authorization.northbound.remote.rest;

import com.janwee.bookstore.authorization.northbound.message.SigningUpRequest;
import com.janwee.bookstore.authorization.northbound.local.UserAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/public/sign-up")
@Tag(name = "Public Signing Up Resources")
@RestController
@RequiredArgsConstructor
@Validated
public class SigningUpResource {

    private final UserAppService userAppService;

    @PostMapping
    @Operation(description = "Register a new user account")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Validated @RequestBody SigningUpRequest request) {
        userAppService.signup(request);
    }
}
