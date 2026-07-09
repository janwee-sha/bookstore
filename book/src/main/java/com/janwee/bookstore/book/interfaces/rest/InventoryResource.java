package com.janwee.bookstore.book.interfaces.rest;

import com.janwee.bookstore.book.application.command.AdjustingStockCommand;
import com.janwee.bookstore.book.application.command.InitializingStockCommand;
import com.janwee.bookstore.book.application.service.InventoryApplicationService;
import com.janwee.bookstore.book.application.view.InventoryView;
import com.janwee.bookstore.foundation.logging.Logging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("inventory")
@Tag(name = "Inventory Resources")
@Validated
@Logging
@RequiredArgsConstructor
public class InventoryResource {
    private final InventoryApplicationService inventoryAppService;

    @GetMapping("/books/{bookId}")
    @Operation(summary = "Retrieve inventory for a book", description = "Retrieve the inventory details for a specific book")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('book:read','book:write')")
    public InventoryView inventoryOfBook(@PathVariable Long bookId) {
        return inventoryAppService.inventoryOfBook(bookId);
    }

    @PostMapping
    @Operation(summary = "Initialize stock", description = "Initialize stock for a book")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('book:write')")
    public void initialize(@Validated @RequestBody InitializingStockCommand command) {
        inventoryAppService.initialize(command);
    }

    @PatchMapping("/books/{bookId}")
    @Operation(summary = "Adjust stock", description = "Adjust stock quantity for a book")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('book:write')")
    public void adjust(@PathVariable Long bookId,
                       @Validated @RequestBody AdjustingStockCommand command) {
        inventoryAppService.adjust(bookId, command);
    }

    @DeleteMapping("/books/{bookId}")
    @Operation(summary = "Delete inventory", description = "Delete inventory for a book")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('book:write')")
    public void delete(@PathVariable Long bookId) {
        inventoryAppService.deleteByBookId(bookId);
    }
}
