package com.janwee.bookstore.order.core.domain;

import com.janwee.bookstore.foundation.exception.NotFoundException;

import java.io.Serial;

public class OrderNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = -5668329758401669443L;

    public OrderNotFoundException(long id) {
        super("No such order of ID: "+id);
    }

}
