package com.janwee.bookstore.book.resource.controller;

import com.janwee.bookstore.book.application.AuthorApplicationService;
import com.janwee.bookstore.book.domain.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authors")
public class AuthorResource {
    private final AuthorApplicationService authorAppService;

    @Autowired
    public AuthorResource(AuthorApplicationService authorAppService) {
        this.authorAppService = authorAppService;
    }

    @GetMapping
    public Author authorOfId(@RequestParam Long id) {
        return authorAppService.authorOfId(id).orElse(null);
    }

    @PostMapping
    public void register(@RequestBody Author author) {
        authorAppService.register(author);
    }
}
