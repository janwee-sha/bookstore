package com.janwee.bookstore.book.infrastructure.messaging;


import com.janwee.bookstore.book.domain.event.*;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import com.janwee.bookstore.foundation.event.Event;
import com.janwee.bookstore.foundation.event.EventPublisher;
import com.janwee.bookstore.book.domain.event.OrderEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
@EnableBinding({EventChannels.class})
public class RabbitOrderEventHandler implements OrderEventHandler {
    private final BookRepository bookRepo;
    private final EventPublisher eventPublisher;

    @Autowired
    public RabbitOrderEventHandler(BookRepository bookRepo,
                                   EventPublisher eventPublisher) {
        this.bookRepo = bookRepo;
        this.eventPublisher = eventPublisher;
    }

    @StreamListener(target = EventChannels.ORDER_IN_CHANNEL,
            condition = "headers['type']=='ORDER_CREATED'")
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void whenOrderCreated(OrderCreated event) {
        log.info("Received OrderCreated event: {}", event);
        Optional<Book> optBook = bookRepo.findById(event.bookId());

        if (optBook.isEmpty() || optBook.get().getAmount() - event.amount() < 0) {
            log.info("Book is not found or out of stock.");
            Event bookSoldOut = new BookSoldOut(event.orderId(), event.bookId());
            eventPublisher.publish(bookSoldOut.description(), bookSoldOut);
            return;
        }

        Book book = optBook.get();
        book.sell(event.amount());
        bookRepo.save(book);
        Event bookOrdered = new BookOrdered(event.orderId(), event.bookId());
        eventPublisher.publish(bookOrdered.description(), bookOrdered);
    }
}
