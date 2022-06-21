package com.janwee.bookstore.bookserver.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookOutOfStockException extends RuntimeException {
    private static final long serialVersionUID = 1223415316791089961L;

    public BookOutOfStockException() {
        super("Book out of stock");
    }

}
