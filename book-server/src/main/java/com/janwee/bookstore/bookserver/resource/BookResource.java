package com.janwee.bookstore.bookserver.resource;

import com.janwee.bookstore.bookserver.application.BookApplicationService;
import com.janwee.bookstore.bookserver.application.BookInfo;
import com.janwee.bookstore.bookserver.domain.Book;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@Api(tags = "Books")
public class BookResource {
    private final BookApplicationService bookService;

    @Autowired
    public BookResource(BookApplicationService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @ApiOperation("Get book by ID")
    public BookInfo book(@RequestParam Long id) {
        return bookService.book(id);
    }

    @PostMapping
    @ApiOperation("Add book")
    public void add(@RequestBody Book book) {
        bookService.add(book);
    }
}
