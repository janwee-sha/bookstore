package com.janwee.bookstore.book.infrastructure.messaging;


import com.janwee.bookstore.book.domain.*;
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
public class EventSubscriber {
    private final BookRepository bookRepo;
    private final EventPublisher eventPublisher;

    @Autowired
    public EventSubscriber(BookRepository bookRepo,
                           EventPublisher eventPublisher) {
        this.bookRepo = bookRepo;
        this.eventPublisher = eventPublisher;
    }

    @StreamListener(target = EventChannels.eventFromOrder,
            condition = "headers['type']=='ORDER_CREATED'")
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void whenOrderCreated(OrderCreated event) {
        log.info("Received OrderCreated event: {}", event);
        Optional<Book> optBook = bookRepo.findById(event.bookId());

        if (!optBook.isPresent() || optBook.get().getAmount() - event.amount() < 0) {
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
