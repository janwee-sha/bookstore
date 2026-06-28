package com.janwee.bookstore.order.southbound.port;

import com.janwee.bookstore.order.domain.Ticket;

public interface TicketRepository {
    void add(Ticket ticket);
}
