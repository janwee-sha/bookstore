package com.janwee.bookstore.book;

import com.janwee.bookstore.book.core.domain.model.Currency;
import com.janwee.bookstore.book.core.domain.model.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PriceUnitTest {
    @Test
    void givenPricesWithSameCurrencyAndAmountShouldBeEqual() {
        Price price1 = Price.of(Currency.USD, BigDecimal.valueOf(0));
        Price price2 = Price.of(Currency.USD, BigDecimal.valueOf(0));
        Price price3 = Price.of(Currency.USD, BigDecimal.valueOf(0.00));
        assertEquals(price1, price2);
        assertEquals(price1, price3);

        Price price5 = Price.of(Currency.USD, BigDecimal.valueOf(0.83));
        Price price6 = Price.of(Currency.USD, BigDecimal.valueOf(0.83));
        Price price7 = Price.of(Currency.USD, BigDecimal.valueOf(0.830));
        assertEquals(price5, price6);
        assertEquals(price5, price7);
    }

    @Test
    void givenPricesWithDifferenceCurrenciesOrAmountsShouldNotBeEqual() {
        Price price1 = Price.of(Currency.USD, BigDecimal.valueOf(0));
        Price price2 = Price.of(Currency.CNY, BigDecimal.valueOf(0));
        Price price3 = Price.of(Currency.JPY, BigDecimal.valueOf(0));
        assertNotEquals(price1, price2);
        assertNotEquals(price1, price3);
        assertNotEquals(price2, price3);

        Price price5 = Price.of(Currency.USD, BigDecimal.valueOf(0));
        Price price6 = Price.of(Currency.USD, BigDecimal.valueOf(0.83));
        Price price7 = Price.of(Currency.USD, BigDecimal.valueOf(0.834));
        assertNotEquals(price5, price6);
        assertNotEquals(price5, price7);
        assertNotEquals(price6, price7);
    }
}
