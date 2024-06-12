package com.janwee.bookstore.order.domain;

import com.janwee.bookstore.foundation.exception.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException() {
        super("No such order");
    }

}
