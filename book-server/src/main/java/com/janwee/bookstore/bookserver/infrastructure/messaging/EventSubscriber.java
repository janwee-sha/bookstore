package com.janwee.bookstore.bookserver.infrastructure.messaging;

import com.janwee.bookstore.bookserver.domain.*;
import com.janwee.bookstore.common.domain.event.DomainEventTypes;
import com.janwee.bookstore.common.domain.event.OrderCreated;
import com.janwee.bookstore.common.domain.event.OrderRejected;
import com.janwee.bookstore.common.domain.event.TicketCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
@EnableBinding({EventChannels.class})
public class EventSubscriber {
    private final TicketRepository ticketRepo;
    private final BookRepository bookRepo;
    private final EventPublisher eventPublisher;

    @Autowired
    public EventSubscriber(TicketRepository ticketRepo, BookRepository bookRepo,
                           EventPublisher eventPublisher) {
        this.ticketRepo = ticketRepo;
        this.bookRepo = bookRepo;
        this.eventPublisher = eventPublisher;
    }

    @StreamListener(target = EventChannels.eventFromOrder,
            condition = "headers['type']=='OrderCreated'")
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void whenOrderCreated(OrderCreated event) {
        log.info("Received OrderCreated event: {}", event);
        Optional<Book> optBook = bookRepo.findById(event.getBookId());

        if (!optBook.isPresent() || optBook.get().getAmount() - event.getAmount() < 0) {
            log.info("Book is not found or out of stock.");
            eventPublisher.publish(DomainEventTypes.ORDER_REJECTED, new OrderRejected(event.getOrderId()));
            return;
        }

        Ticket ticket = new Ticket().ofOrder(event.getOrderId()).ofBook(event.getBookId());
        ticketRepo.save(ticket);
        Book book = optBook.get();
        book.sell(event.getAmount());
        bookRepo.save(book);
        eventPublisher.publish(DomainEventTypes.TICKET_CREATED, new TicketCreated(ticket.getId(), ticket.getOrderId(),
                ticket.getCreateBy()));
    }
}
