package com.janwee.bookstore.order.core.southbound.port;

import com.janwee.bookstore.order.core.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
