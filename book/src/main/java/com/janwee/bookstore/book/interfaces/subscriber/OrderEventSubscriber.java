package com.janwee.bookstore.book.interfaces.subscriber;


import com.janwee.bookstore.book.application.BookApplicationService;
import com.janwee.bookstore.book.application.message.OrderingBookRequest;
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
        OrderingBookRequest request = new OrderingBookRequest();
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
