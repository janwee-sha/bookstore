package com.janwee.bookstore.order.application;

import com.janwee.bookstore.common.domain.event.OrderCreated;
import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderRepository;
import com.janwee.bookstore.order.infrastructure.messaging.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderApplicationService {
    private final OrderRepository orderRepo;
    private final DomainEventPublisher eventPublisher;

    @Autowired
    public OrderApplicationService(OrderRepository orderRepo, DomainEventPublisher eventPublisher) {
        this.orderRepo = orderRepo;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void createOrder(final String bookId) {
        log.info("Creating an order");
        Order order = Order.createOrder();
        order.setBookId(bookId);
        orderRepo.save(order);
        OrderCreated event = new OrderCreated(order.getId(), order.getBookId(), order.getCreateBy());
        eventPublisher.publish("OrderCreated", event);
        log.info("Published domain event: {}.",event);
    }
}
