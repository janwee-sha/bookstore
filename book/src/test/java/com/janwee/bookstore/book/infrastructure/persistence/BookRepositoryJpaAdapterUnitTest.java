package com.janwee.bookstore.book.infrastructure.persistence;

import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Currency;
import com.janwee.bookstore.book.domain.model.Price;
import com.janwee.bookstore.book.infrastructure.persistence.jpa.BookPOJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRepositoryJpaAdapterUnitTest {

    @Mock
    private BookPOJpaRepository jpaRepo;

    @InjectMocks
    private BookRepositoryJpaAdapter adapter;

    @Test
    void shouldRejectAddingBookWithExistingId() {
        Book book = newBook();
        book.assignId(1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.add(book));

        assertEquals("New book must not already have an ID", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdatingBookWithoutId() {
        Book book = newBook();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.update(book));

        assertEquals("Existing book ID is required for update", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdatingMissingBook() {
        Book book = newBook();
        book.assignId(1L);
        when(jpaRepo.existsById(1L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.update(book));

        assertEquals("Existing book must be present before update", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    private static Book newBook() {
        return Book.builder()
                .name("book_a")
                .price(Price.of(Currency.USD, BigDecimal.TEN))
                .publishedAt(LocalDate.of(2020, 1, 1))
                .publisher("publisher-a")
                .authorId(1L)
                .build();
    }
}
