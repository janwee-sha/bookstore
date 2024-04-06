package com.janwee.bookstore.book.domain;

public interface EventSubscriber {
    void whenOrderCreated(OrderCreated event);
}
