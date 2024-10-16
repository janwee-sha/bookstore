package com.janwee.bookstore.order.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(generator = "orders_id_seq")
    private Long id;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "amount")
    private int amount;

    @Column(name = "create_by")
    private LocalDateTime createdBy;

    @Column(name = "state")
    private State state;

    public Order() {
        this.state = State.APPROVAL_PENDING;
        this.createdBy = LocalDateTime.now();
    }

    public Long bookId() {
        return bookId;
    }

    public static Order create() {
        return new Order();
    }

    public Order ofBook(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public Order ofAmount(int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Order amount must not be less than 1");
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
