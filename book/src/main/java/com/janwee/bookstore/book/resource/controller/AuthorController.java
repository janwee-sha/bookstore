package com.janwee.bookstore.book.resource.controller;

import com.janwee.bookstore.book.application.AuthorApplicationService;
import com.janwee.bookstore.book.resource.message.AuthorResponse;
import com.janwee.bookstore.book.resource.message.RegisteringAuthorRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authors")
@Tag(name = "Author Resources")
@Validated
public class AuthorController {
    private final AuthorApplicationService authorAppService;

    @Autowired
    public AuthorController(AuthorApplicationService authorAppService) {
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
