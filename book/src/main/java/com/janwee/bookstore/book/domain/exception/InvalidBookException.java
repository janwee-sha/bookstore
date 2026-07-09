package com.janwee.bookstore.book.domain.exception;


import com.janwee.bookstore.foundation.exception.BadRequestException;

import java.io.Serial;

public class InvalidBookException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = 3803992791295815329L;

    protected InvalidBookException(String code, String message) {
        super(code, message);
    }

    public static InvalidBookException unavailableAuthor() {
        throw new InvalidBookException(ErrorCodes.UNAVAILABLE_AUTHOR, "No such author");
    }

    public static InvalidBookException negativePrice() {
        throw new InvalidBookException(ErrorCodes.NEGATIVE_PRICE, "Price is required and should not be less than 0");
    }
}
