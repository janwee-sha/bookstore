package com.janwee.bookstore.bookserver.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookOutOfStockException extends RuntimeException {
    public BookOutOfStockException() {
        super("Book out of stock");
    }

}
