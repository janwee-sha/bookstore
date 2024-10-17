package com.janwee.bookstore.order.southbound.adapter;

import com.janwee.bookstore.foundation.event.Event;
import com.janwee.bookstore.foundation.event.EventPublisher;
import com.janwee.bookstore.order.southbound.message.OrderCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
@Slf4j
public class RabbitEventPublisher implements EventPublisher {
    private final BlockingQueue<OrderCreated> queueOrderCreated = new LinkedBlockingQueue<>();

    public void publish(Event... events) {
        List<CorrelationData> results = new ArrayList<>();
        Stream.of(events).parallel()
                .forEach(event -> {
                    CorrelationData correlation = new CorrelationData(UUID.randomUUID().toString());
                    results.add(correlation);
                    if (event instanceof OrderCreated) {
                        queueOrderCreated.offer((OrderCreated) event);
                    }
                    log.info("Published event: {}", event);
                });
        results.forEach(correlation -> {
            try {
                CorrelationData.Confirm confirm = correlation.getFuture().get(10, TimeUnit.SECONDS);
                log.info(confirm + " for " + correlation.getId());
                if (correlation.getReturned() != null) {
                    log.error("Message for " + correlation.getId() + " was returned ");

                    // throw some exception to invoke binder retry/error handling

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            } catch (ExecutionException | TimeoutException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Bean
    public Supplier<OrderCreated> orderCreatedSupplier() {
        return queueOrderCreated::poll;
    }
}
