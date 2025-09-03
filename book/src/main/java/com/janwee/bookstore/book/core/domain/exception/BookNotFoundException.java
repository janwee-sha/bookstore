package com.janwee.bookstore.book.core.domain.exception;

import com.janwee.bookstore.foundation.exception.NotFoundException;

import java.io.Serial;

public class BookNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = 6857310511213866473L;

    public BookNotFoundException(long id) {
        super("No such book of ID: " + id);
    }

}