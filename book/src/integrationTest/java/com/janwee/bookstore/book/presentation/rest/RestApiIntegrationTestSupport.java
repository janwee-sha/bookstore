package com.janwee.bookstore.book.presentation.rest;

import com.janwee.bookstore.book.BookApplication;
import com.janwee.bookstore.book.core.domain.model.Author;
import com.janwee.bookstore.book.core.domain.model.Book;
import com.janwee.bookstore.book.core.domain.model.Currency;
import com.janwee.bookstore.book.core.domain.model.Price;
import com.janwee.bookstore.book.core.infrastructure.persistence.jpa.AuthorJpaRepository;
import com.janwee.bookstore.book.core.infrastructure.persistence.jpa.BookJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest(
        classes = BookApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
abstract class RestApiIntegrationTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected AuthorJpaRepository authorRepo;

    @Autowired
    protected BookJpaRepository bookRepo;

    @BeforeEach
    void cleanDatabase() {
        bookRepo.deleteAll();
        authorRepo.deleteAll();
    }

    protected Author saveAuthor(String name, String profile, String phoneNumber) {
        Author author = new Author.Builder()
                .ofName(name)
                .withProfile(profile)
                .withPhoneNumber(phoneNumber)
                .build();
        return authorRepo.saveAndFlush(author);
    }

    protected Book saveBook(String name,
                            int amount,
                            BigDecimal priceAmount,
                            LocalDate publishedAt,
                            String publisher,
                            Long authorId) {
        Book book = new Book.Builder()
                .withName(name)
                .withAmount(amount)
                .withPrice(Price.of(Currency.USD, priceAmount))
                .publishAt(publishedAt)
                .byPublisher(publisher)
                .byAuthor(authorId)
                .build();
        return bookRepo.saveAndFlush(book);
    }
}
