package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.Ticket;
import com.janwee.bookstore.order.southbound.port.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryJpaAdapter implements TicketRepository {
    private final TicketPOJpaRepository jpaRepo;

    @Override
    public void add(Ticket ticket) {
        Assert.notNull(ticket, "Ticket is required");
        Assert.isNull(ticket.id(), "New ticket must not already have an ID");
        TicketPO saved = jpaRepo.save(TicketPOAssembler.toPO(ticket));
        ticket.assignId(saved.getId());
    }
}
