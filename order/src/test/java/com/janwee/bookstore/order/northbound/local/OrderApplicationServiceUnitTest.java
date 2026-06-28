package com.janwee.bookstore.order.northbound.local;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderNotFoundException;
import com.janwee.bookstore.order.domain.State;
import com.janwee.bookstore.order.domain.Ticket;
import com.janwee.bookstore.order.northbound.message.OrderResponse;
import com.janwee.bookstore.order.southbound.port.BookClient;
import com.janwee.bookstore.order.southbound.port.EventPublisher;
import com.janwee.bookstore.order.southbound.port.OrderRepository;
import com.janwee.bookstore.order.southbound.port.TicketRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderApplicationServiceUnitTest {
    @Mock
    private OrderRepository orderRepo;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private BookClient bookClient;

    @Mock
    private TicketRepository ticketRepo;

    @InjectMocks
    private OrderApplicationService service;

    @Test
    void shouldReturnOrderResponsesForOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime createdAt = LocalDateTime.now();
        Order order = new Order(1L, 2L, 3, createdAt, State.APPROVAL_PENDING);
        when(orderRepo.ordersOf(pageable)).thenReturn(new PageImpl<>(List.of(order), pageable, 1));

        Page<OrderResponse> responses = service.orders(pageable);

        OrderResponse response = responses.getContent().get(0);
        assertAll(
                () -> assertEquals(1L, response.getId()),
                () -> assertEquals(2L, response.getBookId()),
                () -> assertEquals(3, response.getAmount()),
                () -> assertEquals(createdAt, response.getCreatedAt()),
                () -> assertEquals(State.APPROVAL_PENDING, response.getState())
        );
    }

    @Test
    void shouldReturnOrderResponseForExistingOrder() {
        LocalDateTime createdAt = LocalDateTime.now();
        Order order = new Order(1L, 2L, 3, createdAt, State.APPROVAL_PENDING);
        when(orderRepo.orderOf(1L)).thenReturn(Optional.of(order));

        OrderResponse response = service.nonNullOrderOfId(1L);

        assertAll(
                () -> assertEquals(1L, response.getId()),
                () -> assertEquals(2L, response.getBookId()),
                () -> assertEquals(3, response.getAmount()),
                () -> assertEquals(createdAt, response.getCreatedAt()),
                () -> assertEquals(State.APPROVAL_PENDING, response.getState())
        );
    }

    @Test
    void shouldThrowWhenOrderDoesNotExist() {
        when(orderRepo.orderOf(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.nonNullOrderOfId(1L));
    }

    @Nested
    class ApproveAfterBookOrdered {
        @Test
        void shouldApproveOrderAndCreateTicketForExistingOrder() {
            LocalDateTime createdAt = LocalDateTime.now();
            Order order = new Order(1L, 2L, 3, createdAt, State.APPROVAL_PENDING);
            when(orderRepo.orderOf(1L)).thenReturn(Optional.of(order));

            service.approveAfterBookOrdered(1L);

            ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
            verify(orderRepo).save(order);
            verify(ticketRepo).save(ticketCaptor.capture());
            Ticket ticket = ticketCaptor.getValue();
            assertAll(
                    () -> assertEquals(State.APPROVED, order.state()),
                    () -> assertEquals(1L, ticket.orderId()),
                    () -> assertEquals(2L, ticket.bookId()),
                    () -> assertNotNull(ticket.createdAt())
            );
        }

        @Test
        void shouldThrowWhenOrderDoesNotExist() {
            when(orderRepo.orderOf(1L)).thenReturn(Optional.empty());

            assertThrows(OrderNotFoundException.class, () -> service.approveAfterBookOrdered(1L));
            verify(orderRepo, never()).save(any());
            verify(ticketRepo, never()).save(any());
        }
    }

    @Nested
    class RejectAfterBookSoldOut {
        @Test
        void shouldRejectExistingOrder() {
            LocalDateTime createdAt = LocalDateTime.now();
            Order order = new Order(1L, 2L, 3, createdAt, State.APPROVAL_PENDING);
            when(orderRepo.orderOf(1L)).thenReturn(Optional.of(order));

            service.rejectAfterBookSoldOut(1L);

            verify(orderRepo).save(order);
            verify(ticketRepo, never()).save(any());
            assertEquals(State.REJECTED, order.state());
        }

        @Test
        void shouldIgnoreMissingOrder() {
            when(orderRepo.orderOf(1L)).thenReturn(Optional.empty());

            service.rejectAfterBookSoldOut(1L);

            verify(orderRepo, never()).save(any());
            verify(ticketRepo, never()).save(any());
        }
    }
}
