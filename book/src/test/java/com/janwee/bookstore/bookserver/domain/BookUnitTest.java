package com.janwee.bookstore.bookserver.domain;

import com.janwee.bookstore.bookserver.infrastructure.validation.ValidationGroup;
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
    private static final BigDecimal DUMMY_DECIMAL = new BigDecimal("1.00");
    private static Validator validator;

    private static Book book1, book2;

    @BeforeAll
    static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    void setUpBookInstance() {
        book1 = new Book();
        book1.setId(DUMMY_LONG);
        book1.setName(DUMMY_STRING);
        book1.setAuthorId(DUMMY_LONG);
        book1.setPublishBy(DUMMY_DATE);
        book1.setPublisher(DUMMY_STRING);

        book2 = new Book();
        book2.setName(DUMMY_STRING);
        book2.setPrice(DUMMY_DECIMAL);
        book2.setAuthorId(DUMMY_LONG);
        book2.setPublishBy(DUMMY_DATE);
        book2.setPublisher(DUMMY_STRING);
    }

    @Test
    void givenNegativePriceShouldReturnConstraintViolations() {
        book1.setPrice(new BigDecimal("-1.00"));
        Set<ConstraintViolation<Book>> violations = validator.validate(book1);
        assertEquals(1, violations.size());
        violations.forEach(violation -> {
            assertEquals("price must not be less than zero", violation.getMessage());
            assertEquals("price", violation.getPropertyPath().toString());
        });
    }

    @Test
    void givenZeroPriceShouldNotReturnConstraintViolations() {
        book1.setPrice(new BigDecimal("0.00"));
        Set<ConstraintViolation<Book>> violations = validator.validate(book1);
        assertEquals(0, violations.size());
    }

    @Test
    void givenPositivePriceShouldNotReturnConstraintViolations() {
        book1.setPrice(new BigDecimal("9.00"));
        Set<ConstraintViolation<Book>> violations = validator.validate(book1);
        assertEquals(0, violations.size());
    }

    @Test
    void givenNullIdShouldNotReturnConstraintViolationsWhenInDefaultGroup() {
        book2.setId(null);
        Set<ConstraintViolation<Book>> violations = validator.validate(book2);
        assertEquals(0, violations.size());
    }

    @Test
    void givenNullIdShouldReturnConstraintViolationsWhenInModificationGroup() {
        book2.setId(null);
        Set<ConstraintViolation<Book>> violations = validator.validate(book2, ValidationGroup.Modification.class);
        assertEquals(1, violations.size());
        violations.forEach(violation -> {
            assertEquals("id required", violation.getMessage());
            assertEquals("id", violation.getPropertyPath().toString());
        });
    }
}
