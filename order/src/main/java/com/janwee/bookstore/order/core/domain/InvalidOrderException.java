package com.janwee.bookstore.order.core.domain;

import com.janwee.bookstore.foundation.exception.BadRequestException;

import java.io.Serial;

public class InvalidOrderException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = 8768131047019183107L;

    private InvalidOrderException(String message) {
        super(message);
    }

    public static InvalidOrderException unavailableBook() {
        return new InvalidOrderException("No such book");
    }
}
