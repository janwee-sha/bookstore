package com.janwee.bookstore.book.core.presentation.resource;

import com.janwee.bookstore.book.core.application.AuthorApplicationService;
import com.janwee.bookstore.book.core.presentation.message.AuthorResponse;
import com.janwee.bookstore.book.core.presentation.message.RegisteringAuthorRequest;
import com.janwee.bookstore.foundation.logging.Logging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    @Operation(description = "Retrieve author of given ID")
    public AuthorResponse authorOfId(@RequestParam Long id) {
        return authorAppService.authorOfId(id).map(AuthorResponse::from).orElse(null);
    }

    @PostMapping
    @Operation(description = "Register new author")
    public void register(@RequestBody RegisteringAuthorRequest request) {
        authorAppService.register(request);
    }
}
