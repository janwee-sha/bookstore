package com.janwee.bookstore.order.northbound.remote.subscriber;

import com.janwee.bookstore.order.northbound.local.OrderApplicationService;
import com.janwee.bookstore.order.northbound.message.StockReservationConfirmed;
import com.janwee.bookstore.order.northbound.message.StockReservationRejected;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitBookEventSubscriber {
    private final OrderApplicationService orderAppService;

    public void onStockReservationConfirmed(StockReservationConfirmed event) {
        log.info("Received StockReservationConfirmed event: {}", event);
        orderAppService.approve(event.orderId());
    }

    public void onStockReservationRejected(StockReservationRejected event) {
        log.info("Received StockReservationRejected event: {}", event);
        orderAppService.reject(event.orderId());
    }

    @Bean
    public Consumer<StockReservationConfirmed> stockReservationConfirmedConsumer() {
        return this::onStockReservationConfirmed;
    }

    @Bean
    public Consumer<StockReservationRejected> stockReservationRejectedConsumer() {
        return this::onStockReservationRejected;
    }
}
