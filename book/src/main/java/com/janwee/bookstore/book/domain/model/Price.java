package com.janwee.bookstore.book.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Price {
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
}
