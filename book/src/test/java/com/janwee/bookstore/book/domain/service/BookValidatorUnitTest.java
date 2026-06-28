package com.janwee.bookstore.book.domain.service;

import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.domain.exception.InvalidBookException;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.AuthorRepository;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookValidatorUnitTest {

    @Mock
    private BookRepository bookRepo;

    @Mock
    private AuthorRepository authorRepo;

    @InjectMocks
    private BookValidator validator;

    @Test
    void shouldPassWhenExistingBookAndAuthorAreAvailable() {
        Book book = newBook(1L, 2L);
        when(bookRepo.hasBookOf(1L)).thenReturn(true);
        when(authorRepo.hasAuthorOf(2L)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validate(book));
    }

    @Test
    void shouldRejectMissingBookWhenValidatingExistingBook() {
        Book book = newBook(1L, 2L);
        when(bookRepo.hasBookOf(1L)).thenReturn(false);

        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> validator.validate(book));

        assertEquals("No such book of ID: 1", ex.getMessage());
    }

    @Test
    void shouldRejectUnavailableAuthor() {
        Book book = newBook(null, 2L);
        when(authorRepo.hasAuthorOf(2L)).thenReturn(false);

        InvalidBookException ex = assertThrows(InvalidBookException.class,
                () -> validator.validate(book));

        assertEquals("No such author", ex.getMessage());
    }

    private static Book newBook(Long id, Long authorId) {
        Book book = Book.builder()
                .name("book_a")
                .amount(1)
                .authorId(authorId)
                .build();
        book.assignId(id);
        return book;
    }
}
