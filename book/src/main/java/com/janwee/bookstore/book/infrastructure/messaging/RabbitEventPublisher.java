package com.janwee.bookstore.book.infrastructure.messaging;

import com.janwee.bookstore.book.domain.event.BookOrdered;
import com.janwee.bookstore.book.domain.event.BookSoldOut;
import com.janwee.bookstore.foundation.event.Event;
import com.janwee.bookstore.foundation.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
@Slf4j
public class RabbitEventPublisher implements EventPublisher {
    private final BlockingQueue<BookSoldOut> queueBookSoldOut = new LinkedBlockingQueue<>();
    private final BlockingQueue<BookOrdered> queueBookOrdered = new LinkedBlockingQueue<>();

    @Override
    public void publish(Event... events) {

        Stream.of(events).parallel().forEach(event -> {
            if (event instanceof BookSoldOut) {
                queueBookSoldOut.offer((BookSoldOut) event);
            } else if (event instanceof BookOrdered) {
                queueBookOrdered.offer((BookOrdered) event);
            }
            log.info("Published event: {}", event);
        });
    }

    @Bean
    public Supplier<BookSoldOut> bookSoldOut() {
        return queueBookSoldOut::poll;
    }

    @Bean
    public Supplier<BookOrdered> bookOrdered() {
        return queueBookOrdered::poll;
    }
}
