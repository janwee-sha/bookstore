package com.janwee.bookstore.book.infrastructure.persistence;

import com.janwee.bookstore.book.domain.model.InventoryItem;
import com.janwee.bookstore.book.domain.repository.InventoryItemRepository;
import com.janwee.bookstore.book.infrastructure.persistence.assembler.InventoryItemPOAssembler;
import com.janwee.bookstore.book.infrastructure.persistence.entity.InventoryItemPO;
import com.janwee.bookstore.book.infrastructure.persistence.jpa.InventoryItemPOJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InventoryItemRepositoryJpaAdapter implements InventoryItemRepository {
    private final InventoryItemPOJpaRepository jpaRepo;

    @Override
    public Optional<InventoryItem> itemOfBookId(Long bookId) {
        return jpaRepo.findByBookId(bookId)
                .map(InventoryItemPOAssembler::toDomain);
    }

    @Override
    public boolean hasItemOfBookId(Long bookId) {
        return jpaRepo.existsByBookId(bookId);
    }

    @Override
    public void add(InventoryItem item) {
        Assert.notNull(item, "InventoryItem is required");
        Assert.isNull(item.id(), "New inventory item must not already have an ID");
        InventoryItemPO saved = jpaRepo.save(InventoryItemPOAssembler.toPO(item));
        item.assignId(saved.getId());
    }

    @Override
    public void update(InventoryItem item) {
        Assert.notNull(item, "InventoryItem is required");
        Long id = item.id();
        Assert.notNull(id, "Existing inventory item ID is required for update");
        Assert.isTrue(jpaRepo.existsById(id), "Existing inventory item must be present before update");
        jpaRepo.save(InventoryItemPOAssembler.toPO(item));
    }

    @Override
    public void deleteByBookId(Long bookId) {
        jpaRepo.deleteByBookId(bookId);
    }
}
