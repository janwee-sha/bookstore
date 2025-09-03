package com.janwee.bookstore.book.core.domain.exception;


import com.janwee.bookstore.foundation.exception.BadRequestException;

import java.io.Serial;

public class InvalidBookException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = 3803992791295815329L;

    protected InvalidBookException(String message) {
        super(message);
    }

    public static InvalidBookException unavailableAuthor() {
        throw new InvalidBookException("No such author");
    }

    public static InvalidBookException validPriceRequired() {
        throw new InvalidBookException("Price is required and should not be less than 0");
    }

    public static InvalidBookException negativeAmount() {
        throw new InvalidBookException("Amount of the book should not be less than 0");
    }

    public static InvalidBookException soldOut() {
        throw new InvalidBookException("Sorry, the book has been sold out");
    }
}
