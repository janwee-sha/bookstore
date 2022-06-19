package com.janwee.bookstore.order.application;

import com.janwee.bookstore.common.domain.event.OrderCreated;
import com.janwee.bookstore.common.domain.exception.HttpException;
import com.janwee.bookstore.order.domain.BookService;
import com.janwee.bookstore.order.domain.DomainEventPublisher;
import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderApplicationService {
    private final OrderRepository orderRepo;
    private final BookService bookService;
    private final DomainEventPublisher eventPublisher;

    @Autowired
    public OrderApplicationService(OrderRepository orderRepo, BookService bookService,
                                   DomainEventPublisher eventPublisher) {
        this.orderRepo = orderRepo;
        this.bookService = bookService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void createOrder(final String bookId, final int amount) {
        log.info("Creating an order");
        if (!bookService.book(bookId).isPresent()) {
            throw new HttpException("Book of ID: " + bookId + " not found", HttpStatus.NOT_FOUND);
        }
        Order order = Order.createPending().ofBook(bookId).ofAmount(amount);
        orderRepo.save(order);
        OrderCreated event = new OrderCreated(order.getId(), order.getBookId(), order.getAmount(), order.getCreateBy());
        eventPublisher.publish("OrderCreated", event);
    }
}
