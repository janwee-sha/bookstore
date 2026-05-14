package com.janwee.bookstore.authorization.core.northbound.remote.rest;

import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.northbound.local.UserAppService;
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
public class UserResource {
    private final UserAppService userAppService;

    @GetMapping
    @Operation(description = "Retrieve all users")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('user:read')")
    public List<User> users() {
        return userAppService.users();
    }

    @GetMapping("/{username}")
    @Operation(description = "Retrieve user of the given username")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('user:read')")
    public User userOf(@PathVariable String username) {
        return userAppService.userOf(username);
    }
}
