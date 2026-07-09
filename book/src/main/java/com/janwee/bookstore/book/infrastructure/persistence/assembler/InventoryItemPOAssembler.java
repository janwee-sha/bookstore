package com.janwee.bookstore.book.infrastructure.persistence.assembler;

import com.janwee.bookstore.book.domain.model.InventoryItem;
import com.janwee.bookstore.book.infrastructure.persistence.entity.InventoryItemPO;

public class InventoryItemPOAssembler {
    public static InventoryItem toDomain(InventoryItemPO po) {
        if (po == null) {
            return null;
        }
        return new InventoryItem(po.getId(), po.getBookId(), po.getQuantity());
    }

    public static InventoryItemPO toPO(InventoryItem item) {
        if (item == null) {
            return null;
        }
        return new InventoryItemPO(item.id(), item.bookId(), item.quantity());
    }
}
