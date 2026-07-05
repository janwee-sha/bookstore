package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.State;
import com.janwee.bookstore.order.southbound.port.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = OrderJpaTestConfiguration.class)
class OrderRepositoryIntegrationTest {
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAndLoadOrder() {
        Order order = new Order.Builder()
                .bookId(1L)
                .amount(2)
                .build();

        orderRepo.save(order);
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
        Order order = new Order.Builder()
                .bookId(1L)
                .amount(2)
                .build();

        orderRepo.save(order);
        entityManager.flush();
        entityManager.clear();

        Order changed = orderRepo.orderOf(order.id()).orElseThrow();
        changed.approve();
        orderRepo.save(changed);
        entityManager.flush();
        entityManager.clear();

        Order updated = orderRepo.orderOf(order.id()).orElseThrow();
        assertEquals(State.APPROVED, updated.state());
    }
}
