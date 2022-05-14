package com.janwee.bookstore.bookserver.resource;

import com.janwee.bookstore.bookserver.application.BookApplicationService;
import com.janwee.bookstore.bookserver.application.BookInfo;
import com.janwee.bookstore.bookserver.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
public class BookResource {
    private final BookApplicationService bookService;

    @Autowired
    public BookResource(BookApplicationService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public BookInfo book(@RequestParam Long id) {
        return bookService.book(id).orElse(null);
    }

    @PostMapping
    public void add(@RequestBody Book book) {
        bookService.add(book);
    }

    @DeleteMapping
    public void remove(@RequestParam Long id){
        bookService.remove(id);
    }
}
