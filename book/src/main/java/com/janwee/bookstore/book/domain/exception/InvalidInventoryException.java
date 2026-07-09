package com.janwee.bookstore.book.domain.exception;

import com.janwee.bookstore.foundation.exception.BadRequestException;

import java.io.Serial;

public class InvalidInventoryException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = 1L;

    protected InvalidInventoryException(String code, String message) {
        super(code, message);
    }

    public static InvalidInventoryException negativeQuantity() {
        throw new InvalidInventoryException(ErrorCodes.NEGATIVE_QUANTITY, "Quantity should not be less than 0");
    }

    public static InvalidInventoryException insufficientStock() {
        throw new InvalidInventoryException(ErrorCodes.INSUFFICIENT_STOCK, "Insufficient stock available");
    }
}
