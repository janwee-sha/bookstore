package com.janwee.bookstore.order.southbound.port;

import com.janwee.bookstore.order.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
