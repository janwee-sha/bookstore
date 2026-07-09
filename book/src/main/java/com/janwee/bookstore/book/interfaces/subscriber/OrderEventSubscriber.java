package com.janwee.bookstore.book.interfaces.subscriber;


import com.janwee.bookstore.book.application.command.ReservingStockCommand;
import com.janwee.bookstore.book.application.service.InventoryApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventSubscriber {
    private final InventoryApplicationService inventoryAppService;

    public void onOrderCreated(OrderCreated event) {
        log.info("Received OrderCreated event: {}", event);
        ReservingStockCommand command = new ReservingStockCommand();
        command.setBookId(event.bookId());
        command.setOrderId(event.orderId());
        command.setAmount(event.amount());
        inventoryAppService.reserve(command);
    }

    @Bean
    public Consumer<OrderCreated> orderCreatedConsumer() {
        return this::onOrderCreated;
    }
}
