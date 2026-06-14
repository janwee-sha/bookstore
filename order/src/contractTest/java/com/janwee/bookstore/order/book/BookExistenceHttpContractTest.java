package com.janwee.bookstore.order.book;

import com.janwee.bookstore.book.BookApplication;
import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Currency;
import com.janwee.bookstore.book.domain.model.Price;
import com.janwee.bookstore.book.infrastructure.persistence.jpa.AuthorJpaRepository;
import com.janwee.bookstore.book.infrastructure.persistence.jpa.BookJpaRepository;
import com.janwee.bookstore.order.southbound.adapter.BookFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = BookApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = {
                "spring.application.name=book",
                "spring.config.import=",
                "spring.cloud.config.enabled=false",
                "spring.cloud.discovery.enabled=false",
                "spring.cloud.stream.enabled=false",
                "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:18083/oauth2/jwks",
                "spring.datasource.url=jdbc:h2:mem:book-contract;MODE=PostgreSQL;INIT=CREATE SCHEMA IF NOT EXISTS book;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
                "eureka.client.enabled=false",
                "management.health.rabbit.enabled=false"
        }
)
@AutoConfigureMockMvc
class BookExistenceHttpContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorJpaRepository authorRepo;

    @Autowired
    private BookJpaRepository bookRepo;

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @BeforeEach
    void cleanDatabase() {
        bookRepo.deleteAll();
        authorRepo.deleteAll();
    }

    @Test
    void orderConsumerUsesBookProviderHeadContract() throws Exception {
        Method checkBook = BookFeignClient.class.getMethod("checkBook", Long.class);

        FeignClient feignClient = BookFeignClient.class.getAnnotation(FeignClient.class);
        assertNotNull(feignClient);
        assertEquals("book", feignClient.value());

        RequestMapping requestMapping = checkBook.getAnnotation(RequestMapping.class);
        assertNotNull(requestMapping);
        assertArrayEquals(new RequestMethod[]{RequestMethod.HEAD}, requestMapping.method());
        assertArrayEquals(new String[]{"books/{id}"}, requestMapping.value());

        PathVariable pathVariable = checkBook.getParameters()[0].getAnnotation(PathVariable.class);
        assertNotNull(pathVariable);
        assertEquals("id", pathVariable.value());
    }

    @Test
    void bookProviderSatisfiesOrderBookExistenceContract() throws Exception {
        Author author = saveAuthor();
        Book book = saveBook(author.id());

        mockMvc.perform(head("/books/{id}", book.id()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(head("/books/{id}", book.id()).with(bookReader()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(head("/books/{id}", 9999L).with(bookReader()))
                .andExpect(status().isNotFound());
    }

    private RequestPostProcessor bookReader() {
        return jwt()
                .jwt(token -> token.claim("scope", List.of("book:read")))
                .authorities(token -> jwtAuthenticationConverter.convert(token).getAuthorities());
    }

    private Author saveAuthor() {
        Author author = new Author.Builder()
                .ofName("Contract Author")
                .withProfile("Contract")
                .withPhoneNumber("13800000010")
                .build();
        return authorRepo.saveAndFlush(author);
    }

    private Book saveBook(Long authorId) {
        Book book = new Book.Builder()
                .withName("Contract Book")
                .withAmount(5)
                .withPrice(Price.of(Currency.USD, new BigDecimal("12.30")))
                .publishAt(LocalDate.of(2026, 5, 12))
                .byPublisher("Contract Publisher")
                .byAuthor(authorId)
                .build();
        return bookRepo.saveAndFlush(book);
    }
}
