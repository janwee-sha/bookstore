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
    public void save(Ticket ticket) {
        Assert.notNull(ticket, "Ticket is required");
        TicketPO saved = jpaRepo.save(TicketPOAssembler.toPO(ticket));
        ticket.assignId(saved.getId());
    }
}
