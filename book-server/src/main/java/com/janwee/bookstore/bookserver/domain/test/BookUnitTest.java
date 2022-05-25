package com.janwee.bookstore.bookserver.domain.test;

import com.janwee.bookstore.bookserver.domain.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookUnitTest {
    private static final String DUMMY_STRING = "dummy";
    private static final Long DUMMY_LONG = 1L;
    private static final LocalDate DUMMY_DATE = LocalDate.of(2022, 1, 1);
    private static Validator validator;

    private static Book book;

    @BeforeAll
    static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    void setUpBookInstance() {
        book = new Book();
        book.setName(DUMMY_STRING);
        book.setAuthorId(DUMMY_LONG);
        book.setPublishBy(DUMMY_DATE);
        book.setPublisher(DUMMY_STRING);
    }

    @Test
    void givenNegativePriceShouldReturnConstraintViolations() {
        book.setPrice(new BigDecimal("-1.00"));
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());
        assertEquals("price must not be less than zero", violations.stream()
                .map(ConstraintViolation::getMessage).findAny()
                .orElse(null));
    }

    @Test
    void givenZeroPriceShouldNotReturnConstraintViolations() {
        book.setPrice(new BigDecimal("0.00"));
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(0, violations.size());
    }

    @Test
    void givenPositivePriceShouldNotReturnConstraintViolations() {
        book.setPrice(new BigDecimal("9.00"));
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(0, violations.size());
    }
}
