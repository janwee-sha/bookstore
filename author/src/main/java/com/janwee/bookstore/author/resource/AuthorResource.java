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
    public Author authorOfId(@RequestParam Long id) {
        return authorService.authorOfId(id).orElse(null);
    }

    @PostMapping
    public void register(@RequestBody Author author) {
        authorService.register(author);
    }
}
