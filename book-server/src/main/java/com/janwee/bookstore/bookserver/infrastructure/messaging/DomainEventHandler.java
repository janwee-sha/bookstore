package com.janwee.bookstore.bookserver.infrastructure.messaging;

import com.janwee.bookstore.bookserver.domain.Ticket;
import com.janwee.bookstore.bookserver.domain.TicketRepository;
import com.janwee.bookstore.common.domain.event.OrderCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@EnableBinding({EventProcessor.class})
public class DomainEventHandler {
    private final TicketRepository ticketRepo;

    @Autowired
    public DomainEventHandler(TicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;

    }

    @StreamListener(target = EventProcessor.eventFromOrder,
            condition = "headers['type']=='OrderCreated'")
    @Transactional(rollbackFor = Throwable.class)
    public void onOrderCreated(OrderCreated event) {
        log.info("Received event: {}", event);
        Ticket ticket = new Ticket().ofOrder(event.getOrderId()).ofBook(event.getBookId());
        ticketRepo.save(ticket);
    }
}
