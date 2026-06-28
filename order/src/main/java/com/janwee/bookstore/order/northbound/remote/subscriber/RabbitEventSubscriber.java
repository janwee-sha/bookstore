package com.janwee.bookstore.order.northbound.remote.subscriber;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderNotFoundException;
import com.janwee.bookstore.order.domain.Ticket;
import com.janwee.bookstore.order.northbound.local.EventSubscriber;
import com.janwee.bookstore.order.northbound.message.BookOrdered;
import com.janwee.bookstore.order.northbound.message.BookSoldOut;
import com.janwee.bookstore.order.southbound.port.OrderRepository;
import com.janwee.bookstore.order.southbound.port.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@Slf4j
public class RabbitEventSubscriber implements EventSubscriber {
    private final OrderRepository orderRepo;
    private final TicketRepository ticketRepo;

    @Autowired
    public RabbitEventSubscriber(OrderRepository orderRepo, TicketRepository ticketRepo) {
        this.orderRepo = orderRepo;
        this.ticketRepo = ticketRepo;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void onBookOrdered(BookOrdered event) {
        log.info("Received BookOrdered event: {}", event);
        Order order = orderRepo.orderOf(event.orderId())
                .orElseThrow(() -> new OrderNotFoundException(event.orderId()));
        order.approve();
        orderRepo.save(order);
        Ticket ticket = new Ticket().ofOrder(event.orderId()).ofBook(event.bookId());
        ticketRepo.save(ticket);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void onBookSoldOut(BookSoldOut event) {
        log.info("Received BookSoldOut event: {}", event);
        Optional<Order> optOrder = orderRepo.orderOf(event.orderId());
        if (optOrder.isPresent()) {
            Order order = optOrder.get();
            order.reject();
            orderRepo.save(order);
        }
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
