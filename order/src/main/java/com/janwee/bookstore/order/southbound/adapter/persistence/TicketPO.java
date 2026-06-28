package com.janwee.bookstore.order.southbound.adapter.persistence;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "tickets")
public class TicketPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2738047778557352396L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tickets")
    @SequenceGenerator(name = "seq_tickets", sequenceName = "order.seq_tickets", allocationSize = 1)
    private Long id;

    private Long orderId;

    private Long bookId;

    private LocalDateTime createdAt;

    public TicketPO() {
        this.createdAt = LocalDateTime.now();
    }

    public TicketPO(Long id, Long orderId, Long bookId, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.createdAt = createdAt;
    }
}
