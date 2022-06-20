package com.janwee.bookstore.common.domain.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketCreated implements DomainEvent {
    private static final long serialVersionUID = 4110753163714395988L;
    private Long ticketId;
    private Long orderId;
    private LocalDateTime createBy;

    public TicketCreated() {
    }

    public TicketCreated(Long ticketId, Long orderId, LocalDateTime createBy) {
        this.ticketId = ticketId;
        this.orderId = orderId;
        this.createBy = createBy;
    }

    @Override
    public String toString() {
        return "TicketCreated{" +
                "ticketId='" + ticketId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", createBy=" + createBy +
                '}';
    }
}
