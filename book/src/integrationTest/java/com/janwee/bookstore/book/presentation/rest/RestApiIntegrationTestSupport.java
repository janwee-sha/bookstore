package com.janwee.bookstore.book.presentation.rest;

import com.janwee.bookstore.book.BookApplication;
import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Currency;
import com.janwee.bookstore.book.domain.model.Price;
import com.janwee.bookstore.book.infrastructure.persistence.assembler.BookPOAssembler;
import com.janwee.bookstore.book.infrastructure.persistence.jpa.AuthorJpaRepository;
import com.janwee.bookstore.book.infrastructure.persistence.jpa.BookPOJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

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
    protected BookPOJpaRepository bookRepo;

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    protected RequestPostProcessor bookReader() {
        return tokenWithScope("book:read");
    }

    protected RequestPostProcessor bookWriter() {
        return tokenWithScope("book:write");
    }

    protected RequestPostProcessor orderWriter() {
        return tokenWithScope("order:write");
    }

    protected RequestPostProcessor tokenWithScope(String scope) {
        return jwt()
                .jwt(token -> token.claim("scope", List.of(scope)))
                .authorities(token -> jwtAuthenticationConverter.convert(token).getAuthorities());
    }

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
        Book book = Book.builder()
                .name(name)
                .amount(amount)
                .price(Price.of(Currency.USD, priceAmount))
                .publishedAt(publishedAt)
                .publisher(publisher)
                .authorId(authorId)
                .build();
        return BookPOAssembler.toDomain(bookRepo.saveAndFlush(BookPOAssembler.toPO(book)));
    }
}
