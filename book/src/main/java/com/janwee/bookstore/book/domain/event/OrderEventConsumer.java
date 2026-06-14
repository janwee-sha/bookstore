package com.janwee.bookstore.book.domain.event;

public interface OrderEventConsumer {
    void onOrderCreated(OrderCreated event);
}
