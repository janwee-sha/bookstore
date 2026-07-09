package com.janwee.bookstore.book.domain.service;

import com.janwee.bookstore.book.domain.exception.InvalidBookException;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookPublicationPolicyUnitTest {

    @Mock
    private AuthorRepository authorRepo;

    @InjectMocks
    private BookPublicationPolicy bookPublicationPolicy;

    @Test
    void shouldPassWhenExistingBookAndAuthorAreAvailable() {
        Book book = newBook(1L, 2L);
        when(authorRepo.hasAuthorOf(2L)).thenReturn(true);

        assertDoesNotThrow(() -> bookPublicationPolicy.check(book));
    }

    @Test
    void shouldRejectUnavailableAuthor() {
        Book book = newBook(null, 2L);
        when(authorRepo.hasAuthorOf(2L)).thenReturn(false);

        InvalidBookException ex = assertThrows(InvalidBookException.class,
                () -> bookPublicationPolicy.check(book));

        assertEquals("No such author", ex.getMessage());
    }

    private static Book newBook(Long id, Long authorId) {
        Book book = Book.builder()
                .name("book_a")
                .authorId(authorId)
                .build();
        book.assignId(id);
        return book;
    }
}
