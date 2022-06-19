package com.janwee.bookstore.bookserver.domain;

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
@Table(name = "tbl_ticket")
public class Ticket {
    @Id
    private String id;
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(name = "create_by", nullable = false)
    private LocalDateTime createBy;

    public Ticket() {
        this.id = UUID.randomUUID().toString();
        this.createBy = LocalDateTime.now();
    }

    public Ticket ofOrder(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public Ticket ofBook(String bookId) {
        this.bookId = bookId;
        return this;
    }
}
