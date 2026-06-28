package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.State;
import com.janwee.bookstore.order.domain.Ticket;
import com.janwee.bookstore.order.southbound.port.OrderRepository;
import com.janwee.bookstore.order.southbound.port.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = OrderJpaTestConfiguration.class)
class OrderRepositoryIntegrationTest {
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAndLoadOrder() {
        Order order = Order.create().ofBook(1L).ofAmount(2);

        orderRepo.add(order);
        entityManager.flush();
        entityManager.clear();

        Optional<Order> loaded = orderRepo.orderOf(order.id());
        assertTrue(loaded.isPresent());
        assertAll(
                () -> assertNotNull(order.id()),
                () -> assertEquals(1L, loaded.get().bookId()),
                () -> assertEquals(2, loaded.get().amount()),
                () -> assertEquals(State.APPROVAL_PENDING, loaded.get().state()),
                () -> assertNotNull(loaded.get().createdAt())
        );
    }

    @Test
    void shouldUpdateExistingOrderState() {
        Order order = Order.create().ofBook(1L).ofAmount(2);
        orderRepo.add(order);
        entityManager.flush();
        entityManager.clear();

        Order changed = orderRepo.orderOf(order.id()).orElseThrow();
        changed.approve();
        orderRepo.update(changed);
        entityManager.flush();
        entityManager.clear();

        Order updated = orderRepo.orderOf(order.id()).orElseThrow();
        assertEquals(State.APPROVED, updated.state());
    }

    @Test
    void shouldSaveTicket() {
        Ticket ticket = new Ticket().ofOrder(10L).ofBook(20L);

        ticketRepo.add(ticket);
        entityManager.flush();
        entityManager.clear();

        TicketPO loaded = entityManager.find(TicketPO.class, ticket.id());
        assertAll(
                () -> assertNotNull(ticket.id()),
                () -> assertEquals(10L, loaded.getOrderId()),
                () -> assertEquals(20L, loaded.getBookId()),
                () -> assertNotNull(loaded.getCreatedAt())
        );
    }
}
