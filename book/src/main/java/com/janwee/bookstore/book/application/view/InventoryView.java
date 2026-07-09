package com.janwee.bookstore.book.application.view;

import com.janwee.bookstore.book.domain.model.InventoryItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Schema(title = "Inventory")
@Getter
@Setter
public class InventoryView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "ID")
    private Long id;

    @Schema(title = "Book ID")
    private Long bookId;

    @Schema(title = "Quantity")
    private int quantity;

    public InventoryView() {
    }

    public static InventoryView from(InventoryItem item) {
        if (item == null) {
            return null;
        }
        InventoryView view = new InventoryView();
        view.id = item.id();
        view.bookId = item.bookId();
        view.quantity = item.quantity();
        return view;
    }
}
