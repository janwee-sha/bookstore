package com.janwee.bookstore.order.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tbl_order")
public class Order {
    @Id
    private String id;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "amount")
    private int amount;

    @Column(name = "create_by")
    private LocalDateTime createBy;

    @Column(name = "state")
    private State state;

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.state = State.APPROVAL_PENDING;
        this.createBy = LocalDateTime.now();
    }

    public static Order createPending() {
        return new Order();
    }

    public Order ofBook(String bookId) {
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

    public void reject(){
        this.state=State.REJECTED;
    }

    public void create(){
        this.state=State.CREATED;
    }
}
