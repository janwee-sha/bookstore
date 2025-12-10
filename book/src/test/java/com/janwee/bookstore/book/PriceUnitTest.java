package com.janwee.bookstore.book;

import com.janwee.bookstore.book.core.domain.model.Currency;
import com.janwee.bookstore.book.core.domain.model.Price;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PriceUnitTest {
    @Nested
    class Equality {
        @ParameterizedTest
        @CsvSource({
                "USD,0,0.00",
                "USD,0.83,0.830",
                "CNY,100,100.0",
                "JPY,7,7.000000"
        })
        void equalPricesAcrossScales(String code, String a1, String a2) {
            Currency c = Currency.valueOf(code);
            Price p1 = Price.of(c, new BigDecimal(a1));
            Price p2 = Price.of(c, new BigDecimal(a2));
            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        void reflexiveSymmetricTransitive() {
            Price p1 = Price.of(Currency.USD, new BigDecimal("0.83"));
            Price p2 = Price.of(Currency.USD, new BigDecimal("0.830"));
            Price p3 = Price.of(Currency.USD, new BigDecimal("0.8300"));
            // reflexive
            assertEquals(p1, p1);
            // symmetric
            assertEquals(p1, p2);
            assertEquals(p2, p1);
            // transitive
            assertEquals(p2, p3);
            assertEquals(p1, p3);
        }

        @Test
        void notEqualToNullOrDifferentType() {
            Price p = Price.of(Currency.JPY, new BigDecimal("1"));
            assertNotEquals(null, p);
            assertNotEquals("1/JPY", p);
        }

        @ParameterizedTest
        @CsvSource({
                // different currencies with same amount
                "USD,CNY,0",
                "USD,JPY,7",
                "CNY,JPY,100.00"
        })
        void differentCurrenciesAreNotEqual(String c1, String c2, String amount) {
            Price p1 = Price.of(Currency.valueOf(c1), new BigDecimal(amount));
            Price p2 = Price.of(Currency.valueOf(c2), new BigDecimal(amount));
            assertNotEquals(p1, p2);
        }

        @ParameterizedTest
        @CsvSource({
                "USD,0,0.01",
                "USD,0.83,0.834",
                "JPY,1,2"
        })
        void sameCurrencyDifferentAmountsNotEqual(String code, String a1, String a2) {
            Currency c = Currency.valueOf(code);
            Price p1 = Price.of(c, new BigDecimal(a1));
            Price p2 = Price.of(c, new BigDecimal(a2));
            assertNotEquals(p1, p2);
        }
    }

    @Nested
    class HashCode {
        @Test
        void equalPricesHaveSameHashCode() {
            Price p1 = Price.of(Currency.CNY, new BigDecimal("100.0"));
            Price p2 = Price.of(Currency.CNY, new BigDecimal("100.000"));
            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }
    }

    @Nested
    class FactoryValidation {
        @ParameterizedTest
        @CsvSource({
                "USD,-1",
                "CNY,-0.01",
                "JPY,-7"
        })
        void negativeAmountIsRejected(String code, String amount) {
            Currency c = Currency.valueOf(code);
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> Price.of(c, new BigDecimal(amount)));
            assertTrue(ex.getMessage().contains(amount));
        }
    }

    @Nested
    class StringRepresentation {
        @Test
        void toStringUsesAmountAsGivenAndCurrencyName() {
            Price p = Price.of(Currency.USD, new BigDecimal("0.830"));
            assertEquals("0.830/USD", p.toString());
            Price p2 = Price.of(Currency.JPY, new BigDecimal("1000"));
            assertEquals("1000/JPY", p2.toString());
        }
    }

    @Nested
    class Accessors {
        @Test
        void gettersReturnValues() {
            BigDecimal amt = new BigDecimal("12.34");
            Price p = Price.of(Currency.USD, amt);
            assertEquals(Currency.USD, p.currency());
            assertEquals(Currency.USD, p.getCurrency());
            assertEquals(0, p.amount().compareTo(amt));
            assertEquals(0, p.getAmount().compareTo(amt));
        }
    }
}
