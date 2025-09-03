package com.janwee.bookstore.book.core.domain.model;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class Price implements Serializable {
    @Serial
    private static final long serialVersionUID = -5756225597894095578L;
    private final Currency currency;
    private final BigDecimal amount;

    private Price(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public static Price of(Currency currency, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Illegal negative price: " + amount);
        }
        return new Price(currency, amount);
    }

    public Currency currency() {
        return currency;
    }

    public BigDecimal amount() {
        return amount;
    }

    @Override
    public String toString() {
        return amount + "/" + currency.name();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;
        return currency == price.currency && amount.compareTo(price.amount) == 0;
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
    }
}
