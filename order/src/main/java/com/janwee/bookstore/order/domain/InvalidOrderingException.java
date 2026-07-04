package com.janwee.bookstore.order.domain;

import com.janwee.bookstore.foundation.exception.BadRequestException;

import java.io.Serial;

public class InvalidOrderingException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = 8768131047019183107L;

    private InvalidOrderingException(String code, String message) {
        super(code, message);
    }

    public static InvalidOrderingException unavailableBook() {
        return new InvalidOrderingException(ErrorCodes.ORDER_BOOK_UNAVAILABLE, "No such book");
    }

    public static InvalidOrderingException negativeAmount() {
        return new InvalidOrderingException(ErrorCodes.NEGATIVE_ORDER_AMOUNT, "Order amount must not be less than 1");
    }

    public static InvalidOrderingException illegalApprovalOperation() {
        return new InvalidOrderingException(ErrorCodes.ILLEGAL_ORDER_APPROVAL, "Order cannot be approved");
    }

    public static InvalidOrderingException illegalRejectionOperation() {
        return new InvalidOrderingException(ErrorCodes.ILLEGAL_ORDER_REJECTION, "Order cannot be rejected");
    }
}
