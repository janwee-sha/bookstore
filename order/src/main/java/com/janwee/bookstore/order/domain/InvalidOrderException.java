package com.janwee.bookstore.order.domain;

import com.janwee.bookstore.foundation.exception.BadRequestException;

public class InvalidOrderException extends BadRequestException {

    private InvalidOrderException(String message) {
        super(message);
    }

    public static InvalidOrderException unavailableBook() {
        return new InvalidOrderException("No such book");
    }
}
