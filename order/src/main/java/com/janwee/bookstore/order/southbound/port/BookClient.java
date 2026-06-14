package com.janwee.bookstore.order.southbound.port;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.southbound.message.BookReview;

public interface BookClient {
    BookReview check(Order order);
}
