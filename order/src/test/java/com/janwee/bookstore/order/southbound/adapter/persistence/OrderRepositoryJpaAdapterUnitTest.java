package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryJpaAdapterUnitTest {

    @Mock
    private OrderPOJpaRepository jpaRepo;

    @InjectMocks
    private OrderRepositoryJpaAdapter adapter;

    @Test
    void shouldAssignGeneratedIdWhenSavingOrder() {
        Order order = Order.create().ofBook(1L).ofAmount(2);
        LocalDateTime createdAt = order.createdAt();
        when(jpaRepo.save(any(OrderPO.class)))
                .thenReturn(new OrderPO(100L, 1L, 2, createdAt, State.APPROVAL_PENDING));

        adapter.add(order);

        assertEquals(100L, order.id());
    }

    @Test
    void shouldRejectNullOrder() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.add(null));

        assertEquals("Order is required", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    @Test
    void shouldRejectAddingOrderWithExistingId() {
        Order order = new Order(1L, 2L, 3, LocalDateTime.now(), State.APPROVAL_PENDING);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.add(order));

        assertEquals("New order must not already have an ID", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdatingOrderWithoutId() {
        Order order = Order.create().ofBook(1L).ofAmount(2);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.update(order));

        assertEquals("Existing order ID is required for update", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdatingMissingOrder() {
        Order order = new Order(1L, 2L, 3, LocalDateTime.now(), State.APPROVAL_PENDING);
        when(jpaRepo.existsById(1L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.update(order));

        assertEquals("Existing order must be present before update", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }
}
