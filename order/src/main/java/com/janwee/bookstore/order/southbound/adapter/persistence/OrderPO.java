package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.InvalidOrderException;
import com.janwee.bookstore.order.domain.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
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

    public Long bookId() {
        return bookId;
    }

    public static OrderPO create() {
        return new OrderPO();
    }

    public OrderPO ofBook(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public OrderPO ofAmount(int amount) {
        if (amount < 1) {
            throw InvalidOrderException.illegalAmount();
        }
        this.amount = amount;
        return this;
    }

    public void reject() {
        this.state = State.REJECTED;
    }

    public void approve() {
        this.state = State.APPROVED;
    }
}
