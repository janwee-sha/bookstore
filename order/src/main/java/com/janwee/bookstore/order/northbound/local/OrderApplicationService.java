package com.janwee.bookstore.order.northbound.local;

import com.janwee.bookstore.foundation.event.EventPublisher;
import com.janwee.bookstore.order.domain.InvalidOrderException;
import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.northbound.message.OrderingRequest;
import com.janwee.bookstore.order.southbound.adapter.RabbitEventPublisher;
import com.janwee.bookstore.order.southbound.message.BookReview;
import com.janwee.bookstore.order.southbound.message.OrderCreated;
import com.janwee.bookstore.order.southbound.port.BookClient;
import com.janwee.bookstore.order.southbound.port.OrderRepository;
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
    private final EventPublisher eventPublisher;

    private final BookClient bookClient;

    @Autowired
    public OrderApplicationService(OrderRepository orderRepo, RabbitEventPublisher eventPublisher,
                                   BookClient bookClient) {
        this.orderRepo = orderRepo;
        this.eventPublisher = eventPublisher;
        this.bookClient = bookClient;
    }

    @Transactional(readOnly = true)
    public Page<Order> orders(Pageable page) {
        log.info("Loading orders");
        return orderRepo.findAll(PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by("createBy").descending()));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void createOrder(OrderingRequest request) {
        log.info("Creating an order");
        Order order = Order.create().ofBook(request.getBookId()).ofAmount(request.getAmount());
        BookReview bookReview = bookClient.check(order);
        if (bookReview.isUnavailable()) {
            throw InvalidOrderException.unavailableBook();
        }
        orderRepo.save(order);
        OrderCreated orderCreated = new OrderCreated(order.getId(), order.getBookId(), order.getAmount(),
                order.getCreatedBy());
        eventPublisher.publish(orderCreated.description(), orderCreated);
    }
}
