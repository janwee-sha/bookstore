package com.janwee.bookstore.book.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookSoldOutException extends RuntimeException {
    private static final long serialVersionUID = 1223415316791089961L;

    public BookSoldOutException() {
        super("Book out of stock");
    }

}
