package com.janwee.bookstore.book.domain.exception;

import com.janwee.bookstore.foundation.exception.NotFoundException;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(long id) {
        super("No such book of ID: " + id);
    }

}