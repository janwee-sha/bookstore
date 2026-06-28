package com.janwee.bookstore.order.northbound.remote.subscriber;

import com.janwee.bookstore.order.northbound.local.OrderApplicationService;
import com.janwee.bookstore.order.northbound.message.BookOrdered;
import com.janwee.bookstore.order.northbound.message.BookSoldOut;
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

    public void onBookOrdered(BookOrdered event) {
        log.info("Received BookOrdered event: {}", event);
        orderAppService.approve(event.orderId());
    }

    public void onBookSoldOut(BookSoldOut event) {
        log.info("Received BookSoldOut event: {}", event);
        orderAppService.reject(event.orderId());
    }

    @Bean
    public Consumer<BookOrdered> bookOrderedConsumer() {
        return this::onBookOrdered;
    }

    @Bean
    public Consumer<BookSoldOut> bookSoldOutConsumer() {
        return this::onBookSoldOut;
    }
}
