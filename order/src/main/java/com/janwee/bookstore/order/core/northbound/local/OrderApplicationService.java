package com.janwee.bookstore.order.core.northbound.local;

import com.janwee.bookstore.foundation.event.EventPublisher;
import com.janwee.bookstore.order.core.domain.InvalidOrderException;
import com.janwee.bookstore.order.core.domain.Order;
import com.janwee.bookstore.order.core.domain.OrderNotFoundException;
import com.janwee.bookstore.order.core.northbound.message.OrderingBookRequest;
import com.janwee.bookstore.order.core.southbound.adapter.RabbitEventPublisher;
import com.janwee.bookstore.order.core.southbound.message.BookReview;
import com.janwee.bookstore.order.core.southbound.message.OrderCreated;
import com.janwee.bookstore.order.core.southbound.port.BookClient;
import com.janwee.bookstore.order.core.southbound.port.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Order> orders(Pageable pageable) {
        log.info("Loading orders");
        return orderRepo.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Order nonNullOrderOfId(long id) {
        return orderRepo.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional(rollbackFor = Throwable.class)
    public long orderBook(OrderingBookRequest request) {
        log.info("Order a book");
        Order order = Order.create().ofBook(request.getBookId()).ofAmount(request.getAmount());
        BookReview bookReview = bookClient.check(order);
        if (bookReview.isUnavailable()) {
            throw InvalidOrderException.unavailableBook();
        }
        orderRepo.saveAndFlush(order);

        OrderCreated orderCreated = new OrderCreated(order.getId(), order.getBookId(), order.getAmount(),
                order.getCreatedAt());
        eventPublisher.publish(orderCreated);
        return order.getId();
    }
}
