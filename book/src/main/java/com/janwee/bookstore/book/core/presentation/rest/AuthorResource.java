package com.janwee.bookstore.book.core.presentation.rest;

import com.janwee.bookstore.book.core.application.AuthorApplicationService;
import com.janwee.bookstore.book.core.application.message.AuthorResponse;
import com.janwee.bookstore.book.core.application.message.RegisteringAuthorRequest;
import com.janwee.bookstore.foundation.logging.Logging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authors")
@Tag(name = "Author Resources")
@Validated
@Logging
public class AuthorResource {
    private final AuthorApplicationService authorAppService;

    @Autowired
    public AuthorResource(AuthorApplicationService authorAppService) {
        this.authorAppService = authorAppService;
    }

    @GetMapping("/{id}")
    @Operation(description = "Retrieve author of given ID")
    @PreAuthorize("hasAnyAuthority('book:read')")
    public AuthorResponse authorOfId(@PathVariable Long id) {
        return authorAppService.authorOfId(id).map(AuthorResponse::from).orElse(null);
    }

    @PostMapping
    @Operation(description = "Register new author")
    @PreAuthorize("hasAnyAuthority('book:write')")
    public void register(@Validated @RequestBody RegisteringAuthorRequest request) {
        authorAppService.register(request);
    }
}
