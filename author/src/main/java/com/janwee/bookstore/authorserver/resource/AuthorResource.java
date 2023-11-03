package com.janwee.bookstore.authorserver.resource;

import com.janwee.bookstore.authorserver.application.AuthorApplicationService;
import com.janwee.bookstore.authorserver.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authors")
public class AuthorResource {
    private final AuthorApplicationService authorService;

    @Autowired
    public AuthorResource(AuthorApplicationService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public Author author(@RequestParam Long id) {
        return authorService.author(id).orElse(null);
    }

    @PostMapping
    public void add(@RequestBody Author author) {
        authorService.register(author);
    }
}
