package com.janwee.bookstore.book.interfaces.subscriber;


import com.janwee.bookstore.book.application.service.BookApplicationService;
import com.janwee.bookstore.book.application.command.OrderingBookCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventSubscriber {
    private final BookApplicationService bookAppService;

    public void onOrderCreated(OrderCreated event) {
        log.info("Received OrderCreated event: {}", event);
        OrderingBookCommand request = new OrderingBookCommand();
        request.setBookId(event.bookId());
        request.setOrderId(event.orderId());
        request.setAmount(event.amount());
        bookAppService.order(event.bookId(), request);

    }

    @Bean
    public Consumer<OrderCreated> orderCreatedConsumer() {
        return this::onOrderCreated;
    }
}
