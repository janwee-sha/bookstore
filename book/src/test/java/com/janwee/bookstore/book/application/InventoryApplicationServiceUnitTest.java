package com.janwee.bookstore.book.application;

import com.janwee.bookstore.book.application.command.AdjustingStockCommand;
import com.janwee.bookstore.book.application.command.InitializingStockCommand;
import com.janwee.bookstore.book.application.command.ReservingStockCommand;
import com.janwee.bookstore.book.application.service.InventoryApplicationService;
import com.janwee.bookstore.book.application.view.InventoryView;
import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.domain.exception.InventoryNotFoundException;
import com.janwee.bookstore.book.domain.model.InventoryItem;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import com.janwee.bookstore.book.domain.repository.InventoryItemRepository;
import com.janwee.bookstore.book.application.event.IntegrationEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import com.janwee.bookstore.foundation.event.IntegrationEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryApplicationServiceUnitTest {

    @Mock
    private InventoryItemRepository inventoryRepo;

    @Mock
    private BookRepository bookRepo;

    @Mock
    private IntegrationEventPublisher eventPublisher;

    @InjectMocks
    private InventoryApplicationService service;

    @Test
    void shouldReturnInventoryViewWhenInventoryExists() {
        InventoryItem item = new InventoryItem(1L, 100L, 10);
        when(inventoryRepo.itemOfBookId(100L)).thenReturn(Optional.of(item));

        InventoryView view = service.inventoryOfBook(100L);

        assertEquals(1L, view.getId());
        assertEquals(100L, view.getBookId());
        assertEquals(10, view.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenInventoryNotFound() {
        when(inventoryRepo.itemOfBookId(100L)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class,
                () -> service.inventoryOfBook(100L));
    }

    @Test
    void shouldInitializeStockForExistingBook() {
        InitializingStockCommand command = new InitializingStockCommand();
        command.setBookId(100L);
        command.setQuantity(10);
        when(bookRepo.hasBookOf(100L)).thenReturn(true);
        when(inventoryRepo.hasItemOfBookId(100L)).thenReturn(false);

        service.initialize(command);

        ArgumentCaptor<InventoryItem> captor = ArgumentCaptor.forClass(InventoryItem.class);
        verify(inventoryRepo).add(captor.capture());
        assertEquals(100L, captor.getValue().bookId());
        assertEquals(10, captor.getValue().quantity());
    }

    @Test
    void shouldRejectInitializingStockForMissingBook() {
        InitializingStockCommand command = new InitializingStockCommand();
        command.setBookId(100L);
        command.setQuantity(10);
        when(bookRepo.hasBookOf(100L)).thenReturn(false);

        assertThrows(BookNotFoundException.class,
                () -> service.initialize(command));

        verify(inventoryRepo, never()).add(any());
    }

    @Test
    void shouldSkipInitializingWhenInventoryAlreadyExists() {
        InitializingStockCommand command = new InitializingStockCommand();
        command.setBookId(100L);
        command.setQuantity(10);
        when(bookRepo.hasBookOf(100L)).thenReturn(true);
        when(inventoryRepo.hasItemOfBookId(100L)).thenReturn(true);

        service.initialize(command);

        verify(inventoryRepo, never()).add(any());
    }

    @Test
    void shouldAdjustStockForExistingInventory() {
        InventoryItem item = new InventoryItem(1L, 100L, 10);
        AdjustingStockCommand command = new AdjustingStockCommand();
        command.setQuantity(5);
        when(inventoryRepo.itemOfBookId(100L)).thenReturn(Optional.of(item));

        service.adjust(100L, command);

        verify(inventoryRepo).update(item);
        assertEquals(5, item.quantity());
    }

    @Test
    void shouldRejectAdjustingStockForMissingInventory() {
        AdjustingStockCommand command = new AdjustingStockCommand();
        command.setQuantity(5);
        when(inventoryRepo.itemOfBookId(100L)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class,
                () -> service.adjust(100L, command));

        verify(inventoryRepo, never()).update(any());
    }

    @Test
    void shouldReserveStockAndPublishConfirmedEvent() {
        InventoryItem item = new InventoryItem(1L, 100L, 10);
        ReservingStockCommand command = new ReservingStockCommand();
        command.setBookId(100L);
        command.setOrderId(200L);
        command.setAmount(3);
        when(inventoryRepo.itemOfBookId(100L)).thenReturn(Optional.of(item));

        service.reserve(command);

        verify(inventoryRepo).update(item);
        assertEquals(7, item.quantity());
        ArgumentCaptor<IntegrationEvent[]> captor = ArgumentCaptor.forClass(IntegrationEvent[].class);
        verify(eventPublisher).publish(captor.capture());
        assertEquals("StockReservationConfirmed", captor.getValue()[0].getClass().getSimpleName());
    }

    @Test
    void shouldPublishRejectedEventWhenInventoryNotFound() {
        ReservingStockCommand command = new ReservingStockCommand();
        command.setBookId(100L);
        command.setOrderId(200L);
        command.setAmount(3);
        when(inventoryRepo.itemOfBookId(100L)).thenReturn(Optional.empty());

        service.reserve(command);

        verify(inventoryRepo, never()).update(any());
        ArgumentCaptor<IntegrationEvent[]> captor = ArgumentCaptor.forClass(IntegrationEvent[].class);
        verify(eventPublisher).publish(captor.capture());
        assertEquals("StockReservationRejected", captor.getValue()[0].getClass().getSimpleName());
    }

    @Test
    void shouldPublishRejectedEventWhenInsufficientStock() {
        InventoryItem item = new InventoryItem(1L, 100L, 2);
        ReservingStockCommand command = new ReservingStockCommand();
        command.setBookId(100L);
        command.setOrderId(200L);
        command.setAmount(3);
        when(inventoryRepo.itemOfBookId(100L)).thenReturn(Optional.of(item));

        service.reserve(command);

        verify(inventoryRepo, never()).update(any());
        ArgumentCaptor<IntegrationEvent[]> captor = ArgumentCaptor.forClass(IntegrationEvent[].class);
        verify(eventPublisher).publish(captor.capture());
        assertEquals("StockReservationRejected", captor.getValue()[0].getClass().getSimpleName());
    }
}
