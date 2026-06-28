package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.State;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orders")
public class OrderPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7785686752351423857L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_orders")
    @SequenceGenerator(name = "seq_orders", sequenceName = "order.seq_orders", allocationSize = 1)
    private Long id;

    private Long bookId;

    private int amount;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private State state;

    public OrderPO() {
        this.state = State.APPROVAL_PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public OrderPO(Long id, Long bookId, int amount, LocalDateTime createdAt, State state) {
        this.id = id;
        this.bookId = bookId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.state = state;
    }
}
