package com.janwee.bookstore.book.domain.event;

public interface EventConsumer {
    void onOrderCreated(OrderCreated event);
}
