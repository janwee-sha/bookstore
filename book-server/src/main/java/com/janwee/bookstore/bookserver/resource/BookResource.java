package com.janwee.bookstore.bookserver.resource;

import com.janwee.bookstore.bookserver.application.BookApplicationService;
import com.janwee.bookstore.bookserver.application.BookInfo;
import com.janwee.bookstore.bookserver.domain.BookNotFoundException;
import com.janwee.bookstore.common.domain.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.function.Function;
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
    public BookInfoPresentation book(@PathVariable final Long id) {
        return new BookInfoPresentation(bookService.nonNullBook(id));
    }

    @Bean
    @RouterOperation(operation = @Operation(description = "ID to Book Function",
            operationId = "idToBook", tags = "idToBook", responses = @ApiResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = BookInfo.class)))))
    public Function<Long, BookInfo> idToBookFunction() {
        return bookService::nonNullBook;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    @Operation(description = "Check book of given ID")
    @ResponseStatus(HttpStatus.OK)
    public void checkBook(@PathVariable final Long id) {
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
    public void remove(@PathVariable Long id) {
        bookService.remove(id);
    }
}
