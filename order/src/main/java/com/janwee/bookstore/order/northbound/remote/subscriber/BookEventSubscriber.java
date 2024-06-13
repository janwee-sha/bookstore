package com.janwee.bookstore.order.northbound.remote.subscriber;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderNotFoundException;
import com.janwee.bookstore.order.domain.Ticket;
import com.janwee.bookstore.order.northbound.message.InputChannels;
import com.janwee.bookstore.order.southbound.message.OutputChannels;
import com.janwee.bookstore.order.northbound.message.BookOrdered;
import com.janwee.bookstore.order.northbound.message.BookSoldOut;
import com.janwee.bookstore.order.southbound.port.OrderRepository;
import com.janwee.bookstore.order.southbound.port.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
@EnableBinding({OutputChannels.class})
public class BookEventSubscriber {
    private final OrderRepository orderRepo;
    private final TicketRepository ticketRepo;

    @Autowired
    public BookEventSubscriber(OrderRepository orderRepo, TicketRepository ticketRepo) {
        this.orderRepo = orderRepo;
        this.ticketRepo = ticketRepo;
    }

    @StreamListener(target = InputChannels.BOOK_IN_CHANNEL,
            condition = "headers['type']=='BOOK_ORDERED'")
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

    @StreamListener(target = InputChannels.BOOK_IN_CHANNEL,
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
