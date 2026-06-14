package com.janwee.bookstore.order.southbound.adapter;

import com.janwee.bookstore.foundation.event.Event;
import com.janwee.bookstore.foundation.event.EventPublisher;
import com.janwee.bookstore.order.southbound.message.OrderCreated;
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
    private final BlockingQueue<OrderCreated> queueOrderCreated = new LinkedBlockingQueue<>();

    public void publish(Event... events) {
        Stream.of(events).parallel().forEach(event -> {
            if (event instanceof OrderCreated) {
                queueOrderCreated.offer((OrderCreated) event);
            }
            log.info("Published event: {}", event);
        });
    }

    @Bean
    public Supplier<OrderCreated> orderCreatedSupplier() {
        return queueOrderCreated::poll;
    }
}
