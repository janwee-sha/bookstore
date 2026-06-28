package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.Ticket;

public class TicketPOAssembler {
    public static Ticket toDomain(TicketPO po) {
        if (po == null) {
            return null;
        }

        return new Ticket(
                po.getId(),
                po.getOrderId(),
                po.getBookId(),
                po.getCreatedAt()
        );
    }

    public static TicketPO toPO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        return new TicketPO(
                ticket.id(),
                ticket.orderId(),
                ticket.bookId(),
                ticket.createdAt()
        );
    }
}
