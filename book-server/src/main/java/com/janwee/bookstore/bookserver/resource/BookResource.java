package com.janwee.bookstore.bookserver.resource;

import com.janwee.bookstore.bookserver.application.BookApplicationService;
import com.janwee.bookstore.bookserver.application.BookInfo;
import com.janwee.bookstore.bookserver.domain.Book;
import com.janwee.bookstore.bookserver.domain.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("books")
@Validated
public class BookResource {
    private final BookApplicationService bookService;

    @Autowired
    public BookResource(BookApplicationService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BookInfo> books(@PositiveOrZero(message = "page must not be less than zero") @RequestParam int page,
                               @Positive(message = "page size must not be less than one") @RequestParam int size) {
        return bookService.books(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookInfo book(@PathVariable final Long id) {
        return bookService.book(id).orElse(null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    @ResponseStatus(HttpStatus.OK)
    public BookInfo checkBook(@PathVariable final Long id) {
        return bookService.book(id).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody Book book) {
        bookService.add(book);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void modify(@RequestBody Book book) throws BookNotFoundException {
        bookService.modify(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable Long id){
        bookService.remove(id);
    }
}
