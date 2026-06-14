package com.janwee.bookstore.book.domain.repository;

import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Currency;
import com.janwee.bookstore.book.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BookJpaTestConfiguration.class)
public class BookRepositoryIntegrationTest {
    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSavingBook() {
        Book book1 = bookOf("book_a", 1, Price.of(Currency.USD, BigDecimal.TEN),
                LocalDate.of(2020, 1, 1), "publisher-a", 1L);

        bookRepo.add(book1);
        entityManager.flush();
        entityManager.clear();

        Optional<Book> book2 = bookRepo.bookOf(book1.id());
        assertTrue(book2.isPresent());
        assertAll(
                () -> assertNotNull(book1.id()),
                () -> assertEquals(book1.name(), book2.get().name()),
                () -> assertEquals(book1.amount(), book2.get().amount()),
                () -> assertEquals(book1.price(), book2.get().price()),
                () -> assertEquals(book1.publisher(), book2.get().publisher()),
                () -> assertEquals(book1.authorId(), book2.get().authorId()),
                () -> assertEquals(book1.publishedAt(), book2.get().publishedAt())
        );
    }

    @Test
    void shouldRejectAddingBookWithExistingId() {
        Book book = bookOf("book_a", 1, Price.of(Currency.USD, BigDecimal.TEN),
                LocalDate.of(2020, 1, 1), "publisher-a", 1L);
        bookRepo.add(book);
        entityManager.flush();
        entityManager.clear();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookRepo.add(book));

        assertEquals("New book must not already have an ID", ex.getMessage());
    }

    @Test
    void shouldUpdateExistingBook() {
        Book book = bookOf("book_a", 1, Price.of(Currency.USD, BigDecimal.TEN),
                LocalDate.of(2020, 1, 1), "publisher-a", 1L);
        bookRepo.add(book);
        entityManager.flush();
        entityManager.clear();

        Book changed = bookRepo.bookOf(book.id()).orElseThrow();
        changed.changeNameTo("book_b");
        changed.changeAmountTo(2);
        changed.changePriceTo(Price.of(Currency.CNY, BigDecimal.ONE));
        changed.changePublicationDate(LocalDate.of(2024, 2, 2));
        changed.changePublisherTo("publisher-b");
        changed.changeAuthorTo(2L);

        bookRepo.update(changed);
        entityManager.flush();
        entityManager.clear();

        Optional<Book> updated = bookRepo.bookOf(book.id());
        assertTrue(updated.isPresent());
        assertAll(
                () -> assertEquals("book_b", updated.get().name()),
                () -> assertEquals(2, updated.get().amount()),
                () -> assertEquals(Price.of(Currency.CNY, BigDecimal.ONE), updated.get().price()),
                () -> assertEquals(LocalDate.of(2024, 2, 2), updated.get().publishedAt()),
                () -> assertEquals("publisher-b", updated.get().publisher()),
                () -> assertEquals(2L, updated.get().authorId())
        );
    }

    @Test
    void shouldRejectUpdatingBookWithoutId() {
        Book book = bookOf("book_a", 1, Price.of(Currency.USD, BigDecimal.TEN),
                LocalDate.of(2020, 1, 1), "publisher-a", 1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookRepo.update(book));

        assertEquals("Existing book ID is required for update", ex.getMessage());
    }

    @Test
    void shouldRejectUpdatingMissingBook() {
        Book book = bookOf("book_a", 1, Price.of(Currency.USD, BigDecimal.TEN),
                LocalDate.of(2020, 1, 1), "publisher-a", 1L);
        bookRepo.add(book);
        entityManager.flush();
        entityManager.clear();

        bookRepo.delete(book.id());
        entityManager.flush();
        entityManager.clear();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookRepo.update(book));

        assertEquals("Existing book must be present before update", ex.getMessage());
    }

    private static Book bookOf(String name, int amount, Price price, LocalDate publishedAt,
                               String publisher, Long authorId) {
        return new Book.Builder()
                .withName(name)
                .withAmount(amount)
                .withPrice(price)
                .publishAt(publishedAt)
                .byPublisher(publisher)
                .byAuthor(authorId)
                .build();
    }
}
