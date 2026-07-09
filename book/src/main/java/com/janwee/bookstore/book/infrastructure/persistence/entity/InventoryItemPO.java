package com.janwee.bookstore.book.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "inventory_items")
@Getter
public class InventoryItemPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inventory_items")
    @SequenceGenerator(name = "seq_inventory_items", sequenceName = "book.seq_inventory_items", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long bookId;

    @Column(nullable = false)
    private int quantity;

    public InventoryItemPO() {
    }

    public InventoryItemPO(Long id, Long bookId, int quantity) {
        this.id = id;
        this.bookId = bookId;
        this.quantity = quantity;
    }
}
