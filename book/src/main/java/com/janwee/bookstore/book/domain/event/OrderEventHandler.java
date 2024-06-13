package com.janwee.bookstore.book.domain.event;

public interface OrderEventHandler {
    void whenOrderCreated(OrderCreated event);
}
