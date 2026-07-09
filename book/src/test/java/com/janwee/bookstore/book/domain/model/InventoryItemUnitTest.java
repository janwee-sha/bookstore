package com.janwee.bookstore.book.domain.model;

import com.janwee.bookstore.book.domain.exception.InvalidInventoryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemUnitTest {

    @Test
    void shouldCreateInventoryItemWithValidQuantity() {
        InventoryItem item = InventoryItem.create(1L, 10);

        assertNull(item.id());
        assertEquals(1L, item.bookId());
        assertEquals(10, item.quantity());
    }

    @Test
    void shouldRejectCreatingInventoryItemWithNegativeQuantity() {
        assertThrows(InvalidInventoryException.class,
                () -> InventoryItem.create(1L, -1));
    }

    @Test
    void shouldCheckAvailability() {
        InventoryItem item = InventoryItem.create(1L, 10);

        assertTrue(item.hasAvailable(5));
        assertTrue(item.hasAvailable(10));
        assertFalse(item.hasAvailable(11));
    }

    @Test
    void shouldReserveStock() {
        InventoryItem item = InventoryItem.create(1L, 10);

        item.reserve(3);

        assertEquals(7, item.quantity());
    }

    @Test
    void shouldRejectReservingMoreThanAvailable() {
        InventoryItem item = InventoryItem.create(1L, 10);

        assertThrows(InvalidInventoryException.class,
                () -> item.reserve(11));

        assertEquals(10, item.quantity());
    }

    @Test
    void shouldRestockInventory() {
        InventoryItem item = InventoryItem.create(1L, 10);

        item.restock(5);

        assertEquals(15, item.quantity());
    }

    @Test
    void shouldRejectRestockingWithNegativeAmount() {
        InventoryItem item = InventoryItem.create(1L, 10);

        assertThrows(InvalidInventoryException.class,
                () -> item.restock(-1));
    }

    @Test
    void shouldAdjustQuantity() {
        InventoryItem item = InventoryItem.create(1L, 10);

        item.adjustTo(5);

        assertEquals(5, item.quantity());
    }

    @Test
    void shouldRejectAdjustingToNegativeQuantity() {
        InventoryItem item = InventoryItem.create(1L, 10);

        assertThrows(InvalidInventoryException.class,
                () -> item.adjustTo(-1));
    }
}
