package com.janwee.bookstore.order.application;

import com.janwee.bookstore.common.domain.event.DomainEventTypes;
import com.janwee.bookstore.common.domain.event.OrderCreated;
import com.janwee.bookstore.order.domain.DomainEventPublisher;
import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional(readOnly = true)
    public Page<Order> orders(Pageable page) {
        log.info("Loading orders");
        return orderRepo.findAll(PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by("createBy").descending()));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void createOrder(final Long bookId, final int amount) {
        log.info("Creating an order");
        Order order = Order.create().ofBook(bookId).ofAmount(amount);
        orderRepo.save(order);
        OrderCreated event = new OrderCreated(order.getId(), order.getBookId(), order.getAmount(), order.getCreateBy());
        eventPublisher.publish(DomainEventTypes.ORDER_CREATED, event);
    }
}
