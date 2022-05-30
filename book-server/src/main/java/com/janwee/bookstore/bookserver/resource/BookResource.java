package com.janwee.bookstore.bookserver.resource;

import com.janwee.bookstore.bookserver.application.BookApplicationService;
import com.janwee.bookstore.bookserver.application.BookInfo;
import com.janwee.bookstore.bookserver.domain.Book;
import com.janwee.bookstore.bookserver.domain.BookNotFoundException;
import com.janwee.bookstore.common.domain.validation.ValidationGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(method = RequestMethod.OPTIONS)
    @ResponseStatus(HttpStatus.OK)
    public void options(HttpServletResponse response) {
        response.addHeader("Allow", RequestMethod.OPTIONS.name());
        response.addHeader("Allow", RequestMethod.GET.name());
        response.addHeader("Allow", RequestMethod.POST.name());
        response.addHeader("Allow", RequestMethod.PUT.name());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    @ResponseStatus(HttpStatus.OK)
    public void optionsToOne(@PathVariable final Long id, HttpServletResponse response) {
        response.addHeader("Allow", RequestMethod.OPTIONS.name());
        response.addHeader("Allow", RequestMethod.GET.name());
        response.addHeader("Allow", RequestMethod.HEAD.name());
        response.addHeader("Allow", RequestMethod.DELETE.name());
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
    public void publish(@Validated @RequestBody Book book) {
        bookService.publish(book);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void edit(@Validated(value = ValidationGroup.Modification.class) @RequestBody Book book)
            throws BookNotFoundException {
        bookService.edit(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable Long id) {
        bookService.remove(id);
    }
}
