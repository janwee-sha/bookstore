package com.janwee.bookstore.book.infrastructure.messaging;


import com.janwee.bookstore.book.domain.event.BookOrdered;
import com.janwee.bookstore.book.domain.event.BookSoldOut;
import com.janwee.bookstore.book.domain.event.OrderCreated;
import com.janwee.bookstore.book.domain.event.OrderEventConsumer;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import com.janwee.bookstore.foundation.event.Event;
import com.janwee.bookstore.foundation.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@Slf4j
public class RabbitOrderEventConsumer implements OrderEventConsumer {
    private final BookRepository bookRepo;
    private final EventPublisher eventPublisher;

    @Autowired
    public RabbitOrderEventConsumer(BookRepository bookRepo,
                                    EventPublisher eventPublisher) {
        this.bookRepo = bookRepo;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void onOrderCreated(OrderCreated event) {
        log.info("Received OrderCreated event: {}", event);
        Optional<Book> optBook = bookRepo.findById(event.bookId());

        if (optBook.isEmpty() || optBook.get().getAmount() - event.amount() < 0) {
            log.info("Book is not found or out of stock.");
            Event bookSoldOut = new BookSoldOut(event.orderId(), event.bookId());
            eventPublisher.publish(bookSoldOut);
            return;
        }

        Book book = optBook.get();
        book.sell(event.amount());
        bookRepo.save(book);
        Event bookOrdered = new BookOrdered(event.orderId(), event.bookId());
        eventPublisher.publish(bookOrdered);
    }

    @Bean
    public Consumer<OrderCreated> orderCreatedConsumer() {
        return this::onOrderCreated;
    }
}
