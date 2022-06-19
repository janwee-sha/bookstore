package com.janwee.bookstore.common.domain.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketCreated implements DomainEvent {
    private static final long serialVersionUID = 153249684583902383L;
    private String ticketId;
    private String orderId;
    private LocalDateTime createBy;

    public TicketCreated(String ticketId, String orderId, LocalDateTime createBy) {
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
