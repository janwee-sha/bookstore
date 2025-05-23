package com.janwee.bookstore.book.presentation.resource;

import com.janwee.bookstore.book.application.BookApplicationService;
import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.domain.model.Book_;
import com.janwee.bookstore.book.presentation.message.BookResponse;
import com.janwee.bookstore.book.presentation.message.PublishingBookRequest;
import com.janwee.bookstore.book.presentation.message.UpdatingBookRequest;
import com.janwee.bookstore.foundation.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@Tag(name = "Book Resources")
@Validated
public class BookResource {
    private final BookApplicationService bookAppService;

    @Autowired
    public BookResource(BookApplicationService bookAppService) {
        this.bookAppService = bookAppService;
    }

    @GetMapping
    @Operation(description = "Retrieve all books")
    @ResponseStatus(HttpStatus.OK)
    @PageableAsQueryParam
    public Page<BookResponse> books(@SortDefault.SortDefaults({@SortDefault(sort = Book_.ID)}) Pageable page) {
        return bookAppService.books(page);
    }

    @GetMapping("/{id}")
    @Operation(description = "Retrieve the details for the book of given ID")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse book(@PathVariable final Long id) {
        return bookAppService.bookOfId(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    @Operation(description = "Check if is there any book of given ID")
    @ResponseStatus(HttpStatus.OK)
    public void hasBookOfId(@PathVariable final Long id) {
        bookAppService.hasBookOfId(id);
    }

    @PostMapping
    @Operation(description = "Publish new book")
    @ResponseStatus(HttpStatus.CREATED)
    public void publish(@Validated @RequestBody PublishingBookRequest request) {
        bookAppService.publish(request);
    }

    @PatchMapping("/{id}")
    @Operation(description = "Change book information")
    @ResponseStatus(HttpStatus.OK)
    public void change(@PathVariable long id,
                       @Validated(value = ValidationGroup.Modification.class) @RequestBody UpdatingBookRequest request)
            throws BookNotFoundException {
        bookAppService.change(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Withdraw book of given ID")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@PathVariable Long id) {
        bookAppService.withdraw(id);
    }
}