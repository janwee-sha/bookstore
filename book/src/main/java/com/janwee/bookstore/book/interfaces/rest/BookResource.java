package com.janwee.bookstore.book.interfaces.rest;

import com.janwee.bookstore.book.application.command.PublishingBookCommand;
import com.janwee.bookstore.book.application.command.UpdatingBookCommand;
import com.janwee.bookstore.book.application.service.BookApplicationService;
import com.janwee.bookstore.book.application.view.BookView;
import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.foundation.logging.Logging;
import com.janwee.bookstore.foundation.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@Tag(name = "Book Resources")
@Validated
@Logging
public class BookResource {
    private final BookApplicationService bookAppService;

    @Autowired
    public BookResource(BookApplicationService bookAppService) {
        this.bookAppService = bookAppService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all books", description = "Retrieve all books")
    @ResponseStatus(HttpStatus.OK)
    @PageableAsQueryParam
    @PreAuthorize("hasAnyAuthority('book:read','book:write')")
    public Page<BookView> books(@SortDefault.SortDefaults({
            @SortDefault(sort = "publishedAt", direction = Sort.Direction.DESC)}) Pageable page) {
        return bookAppService.books(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve book details", description = "Retrieve the details for the book of given ID")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('book:read','book:write')")
    public BookView book(@PathVariable final Long id) {
        return bookAppService.bookOfId(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    @Operation(summary = "Check existence of a book", description = "Check if is there any book of given ID")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('book:read','book:write')")
    public void hasBookOfId(@PathVariable final Long id) {
        bookAppService.hasBookOfId(id);
    }

    @PostMapping
    @Operation(summary = "Publish a new book", description = "Publish new book")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('book:write')")
    public void publish(@Validated @RequestBody PublishingBookCommand request) {
        bookAppService.publish(request);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Change a book", description = "Change book information")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('book:write')")
    public void change(@PathVariable long id,
                       @Validated(value = ValidationGroup.Modification.class)
                       @RequestBody UpdatingBookCommand request)
            throws BookNotFoundException {
        bookAppService.change(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Withdraw a book", description = "Withdraw book of given ID")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('book:write')")
    public void withdraw(@PathVariable Long id) {
        bookAppService.withdraw(id);
    }
}
