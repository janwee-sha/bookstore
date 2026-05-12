package com.janwee.bookstore.book.domain.repository;

import com.janwee.bookstore.book.core.domain.model.Book;
import com.janwee.bookstore.book.core.domain.model.Currency;
import com.janwee.bookstore.book.core.domain.model.Price;
import com.janwee.bookstore.book.core.domain.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
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
        Book book1 = new Book.Builder()
                .withName("author_a")
                .withAmount(1)
                .withPrice(Price.of(Currency.USD, BigDecimal.TEN))
                .byPublisher("publisher-a")
                .byAuthor(1L)
                .build();

        bookRepo.save(book1);
        entityManager.flush();
        entityManager.clear();

        Optional<Book> book2 = bookRepo.findById(book1.id());
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
}
