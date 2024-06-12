package com.janwee.bookstore.book.domain.exception;


import com.janwee.bookstore.foundation.exception.BadRequestException;

public class InvalidBookException extends BadRequestException {

    protected InvalidBookException(String message) {
        super(message);
    }

    public static InvalidBookException invalidAuthor() {
        throw new InvalidBookException("No such author");
    }

    public static InvalidBookException soldOut() {
        throw new InvalidBookException("Sorry, the book has been sold out");
    }
}
