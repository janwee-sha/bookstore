package com.janwee.bookstore.book.infrastructure.messaging;

import com.janwee.bookstore.book.application.message.StockReservationConfirmed;
import com.janwee.bookstore.book.application.message.StockReservationRejected;
import com.janwee.bookstore.book.domain.service.EventPublisher;
import com.janwee.bookstore.foundation.event.IntegrationEvent;
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
    private final BlockingQueue<StockReservationRejected> queueStockReservationRejected = new LinkedBlockingQueue<>();
    private final BlockingQueue<StockReservationConfirmed> queueStockReservationConfirmed = new LinkedBlockingQueue<>();

    @Override
    public void publish(IntegrationEvent... events) {

        Stream.of(events).parallel().forEach(event -> {
            if (event instanceof StockReservationRejected) {
                queueStockReservationRejected.offer((StockReservationRejected) event);
            } else if (event instanceof StockReservationConfirmed) {
                queueStockReservationConfirmed.offer((StockReservationConfirmed) event);
            }
            log.info("Published event: {}", event);
        });
    }

    @Bean
    public Supplier<StockReservationRejected> stockReservationRejectedSupplier() {
        return queueStockReservationRejected::poll;
    }

    @Bean
    public Supplier<StockReservationConfirmed> stockReservationConfirmedSupplier() {
        return queueStockReservationConfirmed::poll;
    }
}
