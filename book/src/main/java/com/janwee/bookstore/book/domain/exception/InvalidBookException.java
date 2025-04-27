package com.janwee.bookstore.book.domain.exception;


import com.janwee.bookstore.foundation.exception.BadRequestException;

public class InvalidBookException extends BadRequestException {

    protected InvalidBookException(String message) {
        super(message);
    }

    public static InvalidBookException unavailableAuthor() {
        throw new InvalidBookException("No such author");
    }

    public static InvalidBookException validPriceRequired() {
        throw new InvalidBookException("Price is required and should not be less than 0");
    }

    public static InvalidBookException negativeAmount() {
        throw new InvalidBookException("Amount of the book should not be less than 0");
    }

    public static InvalidBookException soldOut() {
        throw new InvalidBookException("Sorry, the book has been sold out");
    }
}
