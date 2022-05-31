package com.janwee.bookstore.authorserver.resource;

import com.janwee.bookstore.authorserver.application.AuthorApplicationService;
import com.janwee.bookstore.authorserver.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        authorService.add(author);
    }
}
