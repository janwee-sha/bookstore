package com.janwee.bookstore.order.northbound.local;

import com.janwee.bookstore.order.domain.InvalidOrderException;
import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderNotFoundException;
import com.janwee.bookstore.order.northbound.message.OrderResponse;
import com.janwee.bookstore.order.northbound.message.OrderingBookRequest;
import com.janwee.bookstore.order.southbound.message.BookReview;
import com.janwee.bookstore.order.southbound.message.OrderCreated;
import com.janwee.bookstore.order.southbound.port.BookClient;
import com.janwee.bookstore.order.southbound.port.EventPublisher;
import com.janwee.bookstore.order.southbound.port.OrderRepository;
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
    public OrderApplicationService(OrderRepository orderRepo, EventPublisher eventPublisher,
                                   BookClient bookClient) {
        this.orderRepo = orderRepo;
        this.eventPublisher = eventPublisher;
        this.bookClient = bookClient;
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> orders(Pageable pageable) {
        log.info("Loading orders");
        return orderRepo.ordersOf(pageable)
                .map(OrderResponseAssembler::from);
    }

    @Transactional(readOnly = true)
    public OrderResponse nonNullOrderOfId(long id) {
        return orderRepo.orderOf(id)
                .map(OrderResponseAssembler::from)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional(rollbackFor = Throwable.class)
    public long orderBook(OrderingBookRequest request) {
        log.info("Order a book");
        Order order = Order.create().ofBook(request.getBookId()).ofAmount(request.getAmount());
        BookReview bookReview = bookClient.check(order);
        if (bookReview.isUnavailable()) {
            throw InvalidOrderException.unavailableBook();
        }
        orderRepo.save(order);

        OrderCreated orderCreated = new OrderCreated(order.id(), order.bookId(), order.amount(),
                order.createdAt());
        eventPublisher.publish(orderCreated);
        return order.id();
    }
}
