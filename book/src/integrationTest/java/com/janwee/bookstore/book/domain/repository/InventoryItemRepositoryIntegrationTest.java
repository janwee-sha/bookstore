package com.janwee.bookstore.book.domain.repository;

import com.janwee.bookstore.book.domain.model.InventoryItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BookJpaTestConfiguration.class)
public class InventoryItemRepositoryIntegrationTest {
    @Autowired
    private InventoryItemRepository inventoryRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSavingInventoryItem() {
        InventoryItem item1 = InventoryItem.create(100L, 10);

        inventoryRepo.add(item1);
        entityManager.flush();
        entityManager.clear();

        Optional<InventoryItem> item2 = inventoryRepo.itemOfBookId(100L);
        assertTrue(item2.isPresent());
        assertAll(
                () -> assertNotNull(item1.id()),
                () -> assertEquals(100L, item2.get().bookId()),
                () -> assertEquals(10, item2.get().quantity())
        );
    }

    @Test
    void shouldUpdateExistingInventoryItem() {
        InventoryItem item = InventoryItem.create(100L, 10);
        inventoryRepo.add(item);
        entityManager.flush();
        entityManager.clear();

        InventoryItem existing = inventoryRepo.itemOfBookId(100L).orElseThrow();
        existing.reserve(3);

        inventoryRepo.update(existing);
        entityManager.flush();
        entityManager.clear();

        Optional<InventoryItem> updated = inventoryRepo.itemOfBookId(100L);
        assertTrue(updated.isPresent());
        assertEquals(7, updated.get().quantity());
    }

    @Test
    void shouldCheckExistenceByBookId() {
        InventoryItem item = InventoryItem.create(100L, 10);
        inventoryRepo.add(item);
        entityManager.flush();

        assertTrue(inventoryRepo.hasItemOfBookId(100L));
        assertFalse(inventoryRepo.hasItemOfBookId(999L));
    }

    @Test
    void shouldDeleteByBookId() {
        InventoryItem item = InventoryItem.create(100L, 10);
        inventoryRepo.add(item);
        entityManager.flush();

        inventoryRepo.deleteByBookId(100L);
        entityManager.flush();
        entityManager.clear();

        assertFalse(inventoryRepo.hasItemOfBookId(100L));
    }
}
