package com.janwee.bookstore.order.infrastructure.messaging;

import com.janwee.bookstore.order.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
@EnableBinding({EventChannels.class})
public class EventSubscriber {
    private final OrderRepository orderRepo;
    private final TicketRepository ticketRepo;

    @Autowired
    public EventSubscriber(OrderRepository orderRepo, TicketRepository ticketRepo) {
        this.orderRepo = orderRepo;
        this.ticketRepo = ticketRepo;
    }

    @StreamListener(target = EventChannels.eventFromBook,
            condition = "headers['type']=='TicketCreated'")
    @Transactional(rollbackFor = Throwable.class)
    public void whenBookOrdered(BookOrdered event) {
        log.info("Received TicketCreated event: {}", event);
        Order order = orderRepo.findById(event.orderId())
                .orElseThrow(OrderNotFoundException::new);
        order.approve();
        orderRepo.save(order);
        Ticket ticket = new Ticket().ofOrder(event.orderId()).ofBook(event.bookId());
        ticketRepo.save(ticket);
    }

    @StreamListener(target = EventChannels.eventFromBook,
            condition = "headers['type']=='BOOK_SOLD_OUT'")
    @Transactional(rollbackFor = Throwable.class)
    public void whenBookSoldOut(BookSoldOut event) {
        log.info("Received OrderRejected event: {}", event);
        Optional<Order> optOrder = orderRepo.findById(event.orderId());
        if (optOrder.isPresent()) {
            Order order = optOrder.get();
            order.reject();
            orderRepo.save(order);
        }
    }
}
