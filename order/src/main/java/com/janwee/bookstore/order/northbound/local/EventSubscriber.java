package com.janwee.bookstore.order.northbound.local;

import com.janwee.bookstore.order.northbound.message.BookOrdered;
import com.janwee.bookstore.order.northbound.message.BookSoldOut;
import org.springframework.transaction.annotation.Transactional;

public interface EventSubscriber {
    @Transactional(rollbackFor = Throwable.class)
    void onBookOrdered(BookOrdered event);

    @Transactional(rollbackFor = Throwable.class)
    void onBookSoldOut(BookSoldOut event);
}
