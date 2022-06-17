package com.janwee.bookstore.order.application;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderCreated;
import com.janwee.bookstore.order.domain.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderApplicationService {
    private final OrderRepository orderRepo;
    private final Processor processor;

    @Autowired
    public OrderApplicationService(OrderRepository orderRepo, Processor processor) {
        this.orderRepo = orderRepo;
        this.processor = processor;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void createOrder(final String bookId) {
        log.info("Creating an order");
        Order order = Order.createOrder();
        order.setBookId(bookId);
        orderRepo.save(order);
        OrderCreated event = new OrderCreated(order);
        processor.output()
                .send(MessageBuilder.withPayload(event)
                        .build());
        log.info("Published domain event: {}.",event);
    }
}
