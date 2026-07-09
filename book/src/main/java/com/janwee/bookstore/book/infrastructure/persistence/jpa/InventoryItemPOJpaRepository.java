package com.janwee.bookstore.book.infrastructure.persistence.jpa;

import com.janwee.bookstore.book.infrastructure.persistence.entity.InventoryItemPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemPOJpaRepository extends JpaRepository<InventoryItemPO, Long> {
    Optional<InventoryItemPO> findByBookId(Long bookId);

    boolean existsByBookId(Long bookId);

    void deleteByBookId(Long bookId);
}
