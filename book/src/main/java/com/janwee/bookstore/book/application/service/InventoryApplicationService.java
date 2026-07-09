package com.janwee.bookstore.book.application.service;

import com.janwee.bookstore.book.application.command.AdjustingStockCommand;
import com.janwee.bookstore.book.application.command.InitializingStockCommand;
import com.janwee.bookstore.book.application.command.ReservingStockCommand;
import com.janwee.bookstore.book.application.message.StockReservationConfirmed;
import com.janwee.bookstore.book.application.message.StockReservationRejected;
import com.janwee.bookstore.book.application.view.InventoryView;
import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.domain.exception.InventoryNotFoundException;
import com.janwee.bookstore.book.domain.model.InventoryItem;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import com.janwee.bookstore.book.domain.repository.InventoryItemRepository;
import com.janwee.bookstore.book.domain.service.EventPublisher;
import com.janwee.bookstore.foundation.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryApplicationService {
    private final InventoryItemRepository inventoryRepo;
    private final BookRepository bookRepo;
    private final EventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public InventoryView inventoryOfBook(long bookId) {
        log.info("Loading inventory for book: {}.", bookId);
        return inventoryRepo.itemOfBookId(bookId)
                .map(InventoryView::from)
                .orElseThrow(() -> new InventoryNotFoundException(bookId));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void initialize(InitializingStockCommand command) {
        log.info("Initializing stock for book: {}.", command.getBookId());

        if (!bookRepo.hasBookOf(command.getBookId())) {
            throw new BookNotFoundException(command.getBookId());
        }

        if (inventoryRepo.hasItemOfBookId(command.getBookId())) {
            log.warn("Inventory already exists for book: {}. Skipping initialization.", command.getBookId());
            return;
        }

        InventoryItem item = InventoryItem.create(command.getBookId(), command.getQuantity());
        inventoryRepo.add(item);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void adjust(long bookId, AdjustingStockCommand command) {
        log.info("Adjusting stock for book: {}.", bookId);

        InventoryItem item = inventoryRepo.itemOfBookId(bookId)
                .orElseThrow(() -> new InventoryNotFoundException(bookId));

        item.adjustTo(command.getQuantity());
        inventoryRepo.update(item);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void reserve(ReservingStockCommand command) {
        log.info("Reserving stock for order: {}, book: {}.", command.getOrderId(), command.getBookId());

        var optItem = inventoryRepo.itemOfBookId(command.getBookId());

        if (optItem.isEmpty() || !optItem.get().hasAvailable(command.getAmount())) {
            log.info("Inventory not found or insufficient stock for book: {}.", command.getBookId());
            Event rejected = new StockReservationRejected(command.getOrderId(), command.getBookId());
            eventPublisher.publish(rejected);
            return;
        }

        InventoryItem item = optItem.get();
        item.reserve(command.getAmount());
        inventoryRepo.update(item);

        Event confirmed = new StockReservationConfirmed(command.getOrderId(), command.getBookId());
        eventPublisher.publish(confirmed);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByBookId(long bookId) {
        log.info("Deleting inventory for book: {}.", bookId);
        inventoryRepo.deleteByBookId(bookId);
    }
}
