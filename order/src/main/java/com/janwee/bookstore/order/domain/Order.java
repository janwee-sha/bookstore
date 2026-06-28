package com.janwee.bookstore.order.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = -7785686752351423857L;

    private Long id;

    private Long bookId;

    private int amount;

    private final LocalDateTime createdAt;

    private State state;

    public Order() {
        this.state = State.APPROVAL_PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Order(Long id, Long bookId, int amount, LocalDateTime createdAt, State state) {
        this.id = id;
        this.bookId = bookId;
        this.changeAmountTo(amount);
        this.createdAt = createdAt;
        this.state = state;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public Long bookId() {
        return bookId;
    }

    public int amount() {
        return amount;
    }

    public void changeAmountTo(int newAmount) {
        if (newAmount < 1) {
            throw InvalidOrderException.illegalAmount();
        }
        this.amount = newAmount;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public State state() {
        return state;
    }

    public static Order create() {
        return new Order();
    }

    public Order ofBook(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public Order ofAmount(int amount) {
        this.changeAmountTo(amount);
        return this;
    }

    public void reject() {
        this.state = State.REJECTED;
    }

    public void approve() {
        this.state = State.APPROVED;
    }
}
