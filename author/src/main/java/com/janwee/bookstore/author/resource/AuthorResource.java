package com.janwee.bookstore.author.resource;

import com.janwee.bookstore.author.application.AuthorApplicationService;
import com.janwee.bookstore.author.domain.Author;
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
