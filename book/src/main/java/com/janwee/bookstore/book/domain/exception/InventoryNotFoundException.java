package com.janwee.bookstore.book.domain.exception;

import com.janwee.bookstore.foundation.exception.NotFoundException;

import java.io.Serial;

public class InventoryNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InventoryNotFoundException(long bookId) {
        super(ErrorCodes.INVENTORY_NOT_FOUND, "No inventory found for book ID: " + bookId);
    }
}
