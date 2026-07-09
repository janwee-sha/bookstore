package com.janwee.bookstore.book.domain.repository;

import com.janwee.bookstore.book.domain.model.InventoryItem;

import java.util.Optional;

public interface InventoryItemRepository {
    Optional<InventoryItem> itemOfBookId(Long bookId);

    boolean hasItemOfBookId(Long bookId);

    void add(InventoryItem item);

    void update(InventoryItem item);

    void deleteByBookId(Long bookId);
}
