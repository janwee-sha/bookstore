package com.janwee.bookstore.book.domain.model;

import com.janwee.bookstore.book.domain.exception.InvalidInventoryException;

import java.io.Serial;
import java.io.Serializable;

public class InventoryItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long bookId;

    private int quantity;

    public InventoryItem() {
    }

    public InventoryItem(Long id, Long bookId, int quantity) {
        this.id = id;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public static InventoryItem create(Long bookId, int initialQuantity) {
        if (initialQuantity < 0) {
            throw InvalidInventoryException.negativeQuantity();
        }
        return new InventoryItem(null, bookId, initialQuantity);
    }

    public Long id() {
        return id;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long bookId() {
        return bookId;
    }

    public int quantity() {
        return quantity;
    }

    public boolean hasAvailable(int amount) {
        return this.quantity >= amount;
    }

    public void reserve(int amount) {
        if (!hasAvailable(amount)) {
            throw InvalidInventoryException.insufficientStock();
        }
        this.quantity -= amount;
    }

    public void restock(int amount) {
        if (amount < 0) {
            throw InvalidInventoryException.negativeQuantity();
        }
        this.quantity += amount;
    }

    public void adjustTo(int quantity) {
        if (quantity < 0) {
            throw InvalidInventoryException.negativeQuantity();
        }
        this.quantity = quantity;
    }
}
