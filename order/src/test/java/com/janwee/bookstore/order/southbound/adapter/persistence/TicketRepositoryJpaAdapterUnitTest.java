package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.Ticket;
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
class TicketRepositoryJpaAdapterUnitTest {

    @Mock
    private TicketPOJpaRepository jpaRepo;

    @InjectMocks
    private TicketRepositoryJpaAdapter adapter;

    @Test
    void shouldAssignGeneratedIdWhenSavingTicket() {
        Ticket ticket = new Ticket().ofOrder(10L).ofBook(20L);
        LocalDateTime createdAt = ticket.createdAt();
        when(jpaRepo.save(any(TicketPO.class)))
                .thenReturn(new TicketPO(100L, 10L, 20L, createdAt));

        adapter.add(ticket);

        assertEquals(100L, ticket.id());
    }

    @Test
    void shouldRejectNullTicket() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.add(null));

        assertEquals("Ticket is required", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    @Test
    void shouldRejectAddingTicketWithExistingId() {
        Ticket ticket = new Ticket(1L, 10L, 20L, LocalDateTime.now());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.add(ticket));

        assertEquals("New ticket must not already have an ID", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }
}
