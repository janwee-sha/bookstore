package com.janwee.bookstore.order.southbound.port;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.southbound.message.BookReview;

/**
 * TODO Add a description for the class here
 *
 * @author Will Hsia
 * @since 2024/6/13
 */
public interface BookClient {
    BookReview check(Order order);
}
