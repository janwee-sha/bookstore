package com.janwee.bookstore.bookserver.application;

import com.janwee.bookstore.bookserver.domain.Ticket;
import com.janwee.bookstore.bookserver.domain.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TicketApplicationService {
    private final TicketRepository ticketRepo;

    @Autowired
    public TicketApplicationService(TicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void print(Ticket ticket) {
        log.info("Printing ticket.");
        ticketRepo.save(ticket);
    }
}
