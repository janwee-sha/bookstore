package com.janwee.bookstore.order.domain;

import com.janwee.bookstore.foundation.exception.BadRequestException;

import java.io.Serial;

public class InvalidOrderingException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = 8768131047019183107L;

    private InvalidOrderingException(String message) {
        super(message);
    }

    public static InvalidOrderingException unavailableBook() {
        return new InvalidOrderingException("No such book");
    }

    public static InvalidOrderingException negativeAmount() {
        return new InvalidOrderingException("Order amount must not be less than 1");
    }

    public static InvalidOrderingException illegalApprovalOperation() {
        return new InvalidOrderingException("Order cannot be approved");
    }

    public static InvalidOrderingException illegalRejectionOperation() {
        return new InvalidOrderingException("Order cannot be rejected");
    }
}
