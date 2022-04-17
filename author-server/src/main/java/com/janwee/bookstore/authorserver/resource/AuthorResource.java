package com.janwee.bookstore.authorserver.resource;

import com.janwee.bookstore.authorserver.application.AuthorApplicationService;
import com.janwee.bookstore.authorserver.domain.Author;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authors")
@Api(tags = "Authors")
public class AuthorResource {
    private final AuthorApplicationService authorService;

    @Autowired
    public AuthorResource(AuthorApplicationService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @ApiOperation("Get author by ID")
    public Author author(@RequestParam Long id) {
        return authorService.author(id).orElse(null);
    }

    @PostMapping
    @ApiOperation("Add author")
    public void add(@RequestBody Author author) {
        authorService.add(author);
    }
}
