package com.janwee.bookstore.book.application;

import com.janwee.bookstore.book.application.view.BookView;
import com.janwee.bookstore.book.application.assembler.BookViewAssembler;
import com.janwee.bookstore.book.application.command.PublishingBookCommand;
import com.janwee.bookstore.book.application.command.UpdatingBookCommand;
import com.janwee.bookstore.book.application.service.BookApplicationService;
import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Currency;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import com.janwee.bookstore.book.domain.service.BookValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookApplicationServiceUnitTest {

    @Mock
    private BookRepository bookRepo;

    @Mock
    private BookValidator bookValidator;

    @Mock
    private BookViewAssembler bookViewAssembler;

    @InjectMocks
    private BookApplicationService service;

    @Test
    void shouldPassBookExistenceCheckWhenBookExists() {
        when(bookRepo.hasBookOf(1L)).thenReturn(true);

        assertDoesNotThrow(() -> service.hasBookOfId(1L));
    }

    @Test
    void shouldRejectBookExistenceCheckWhenBookIsMissing() {
        when(bookRepo.hasBookOf(1L)).thenReturn(false);

        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> service.hasBookOfId(1L));

        assertEquals("No such book of ID: 1", ex.getMessage());
    }

    @Test
    void shouldReturnBookResponseWhenBookExists() {
        Book book = newBook(1L);
        BookView response = new BookView();
        when(bookRepo.bookOf(1L)).thenReturn(Optional.of(book));
        when(bookViewAssembler.assemble(book)).thenReturn(response);

        assertSame(response, service.bookOfId(1L));
    }

    @Test
    void shouldRejectLoadingMissingBook() {
        when(bookRepo.bookOf(1L)).thenReturn(Optional.empty());

        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> service.bookOfId(1L));

        assertEquals("No such book of ID: 1", ex.getMessage());
    }

    @Test
    void shouldValidateAndAddPublishedBook() {
        PublishingBookCommand request = newPublishingBookRequest();

        service.publish(request);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookValidator).validate(captor.capture());
        verify(bookRepo).add(captor.getValue());
        assertEquals("book_a", captor.getValue().name());
        assertEquals(2L, captor.getValue().authorId());
    }

    @Test
    void shouldRejectChangingMissingBook() {
        when(bookRepo.bookOf(1L)).thenReturn(Optional.empty());

        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> service.change(1L, new UpdatingBookCommand()));

        assertEquals("No such book of ID: 1", ex.getMessage());
        verify(bookValidator, never()).validate(any());
        verify(bookRepo, never()).update(any());
    }

    @Test
    void shouldValidateAndUpdateChangedBook() {
        Book book = newBook(1L);
        UpdatingBookCommand request = new UpdatingBookCommand();
        request.setAmount(3);
        when(bookRepo.bookOf(1L)).thenReturn(Optional.of(book));

        service.change(1L, request);

        verify(bookValidator).validate(book);
        verify(bookRepo).update(book);
        assertEquals(3, book.amount());
    }

    @Test
    void shouldIgnoreWithdrawingMissingBook() {
        when(bookRepo.hasBookOf(1L)).thenReturn(false);

        service.withdraw(1L);

        verify(bookRepo, never()).delete(1L);
    }

    @Test
    void shouldDeleteBookWhenWithdrawingExistingBook() {
        when(bookRepo.hasBookOf(1L)).thenReturn(true);

        service.withdraw(1L);

        verify(bookRepo).delete(1L);
    }

    private static Book newBook(Long id) {
        Book book = Book.builder()
                .name("book_a")
                .amount(1)
                .authorId(2L)
                .build();
        book.assignId(id);
        return book;
    }

    private static PublishingBookCommand newPublishingBookRequest() {
        PublishingBookCommand.PriceRequest price = new PublishingBookCommand.PriceRequest();
        price.setCurrency(Currency.USD);
        price.setAmount(BigDecimal.TEN);

        PublishingBookCommand request = new PublishingBookCommand();
        request.setName("book_a");
        request.setAmount(1);
        request.setPrice(price);
        request.setPublisher("publisher-a");
        request.setAuthorId(2L);
        return request;
    }
}
