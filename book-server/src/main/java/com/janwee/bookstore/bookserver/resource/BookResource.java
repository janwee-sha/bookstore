package com.janwee.bookstore.bookserver.resource;

import com.janwee.bookstore.bookserver.application.BookApplicationService;
import com.janwee.bookstore.bookserver.application.BookInfo;
import com.janwee.bookstore.common.domain.exception.BookNotFoundException;
import com.janwee.bookstore.common.domain.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@RestController
@RequestMapping("books")
@Tag(name = "Book resource")
@Validated
public class BookResource {
    private final BookApplicationService bookService;

    @Autowired
    public BookResource(BookApplicationService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    @Operation(description = "Allowed options", deprecated = true)
    @ResponseStatus(HttpStatus.OK)
    public void options(HttpServletResponse response) {
        response.addHeader("Allow", RequestMethod.OPTIONS.name());
        response.addHeader("Allow", RequestMethod.GET.name());
        response.addHeader("Allow", RequestMethod.POST.name());
        response.addHeader("Allow", RequestMethod.PUT.name());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    @Operation(description = "Allowed option to single book")
    @ResponseStatus(HttpStatus.OK)
    public void optionsToOne(@PathVariable final Long id, HttpServletResponse response) {
        response.addHeader("Allow", RequestMethod.OPTIONS.name());
        response.addHeader("Allow", RequestMethod.GET.name());
        response.addHeader("Allow", RequestMethod.HEAD.name());
        response.addHeader("Allow", RequestMethod.DELETE.name());
    }

    @GetMapping
    @Operation(description = "Books")
    @ResponseStatus(HttpStatus.OK)
    @PageableAsQueryParam
    public Page<BookInfoPresentation> books(Pageable page) {
        Page<BookInfo> bookPage = bookService.books(page);
        return new PageImpl<>(bookPage.getContent().stream()
                .map(BookInfoPresentation::new).collect(Collectors.toList()),
                bookPage.getPageable(), bookPage.getTotalElements());
    }

    @GetMapping("/{id}")
    @Operation(description = "Book of given ID")
    @ResponseStatus(HttpStatus.OK)
    public BookInfoPresentation book(@PathVariable final String id) {
        return new BookInfoPresentation(bookService.nonNullBook(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    @Operation(description = "Check book of given ID")
    @ResponseStatus(HttpStatus.OK)
    public void checkBook(@PathVariable final String id) {
        bookService.checkExistenceOfBook(id);
    }

    @PostMapping
    @Operation(description = "Publish new book")
    @ResponseStatus(HttpStatus.CREATED)
    public void publish(@Validated @RequestBody SavingBookCommand cmd) {
        bookService.publish(cmd.toBook());
    }

    @PutMapping
    @Operation(description = "Edit book")
    @ResponseStatus(HttpStatus.OK)
    public void edit(@Validated(value = ValidationGroup.Modification.class) @RequestBody SavingBookCommand cmd)
            throws BookNotFoundException {
        bookService.edit(cmd.toBook());
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Remove book of given ID")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable String id) {
        bookService.remove(id);
    }
}
