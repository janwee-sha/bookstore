package com.janwee.bookstore.order.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderUnitTest {

    @Test
    void shouldCreatePendingOrderWithCurrentCreationTime() {
        LocalDateTime beforeCreate = LocalDateTime.now();

        Order order = new Order();

        LocalDateTime afterCreate = LocalDateTime.now();
        assertAll(
                () -> assertNull(order.id()),
                () -> assertNull(order.bookId()),
                () -> assertEquals(0, order.amount()),
                () -> assertEquals(State.APPROVAL_PENDING, order.state()),
                () -> assertNotNull(order.createdAt()),
                () -> assertCreatedBetween(order.createdAt(), beforeCreate, afterCreate)
        );
    }

    @Test
    void shouldRebuildPersistedOrder() {
        LocalDateTime createdAt = LocalDateTime.now();

        Order order = new Order(1L, 2L, 3, createdAt, State.APPROVED);

        assertAll(
                () -> assertEquals(1L, order.id()),
                () -> assertEquals(2L, order.bookId()),
                () -> assertEquals(3, order.amount()),
                () -> assertEquals(createdAt, order.createdAt()),
                () -> assertEquals(State.APPROVED, order.state())
        );
    }

    @Test
    void shouldAssignId() {
        Order order = new Order();

        order.assignId(10L);

        assertEquals(10L, order.id());
    }

    @Nested
    class ChangeAmount {

        @Test
        void shouldChangeAmountWhenPositive() {
            Order order = new Order();

            order.changeAmountTo(2);

            assertEquals(2, order.amount());
        }

        @Test
        void shouldRejectZeroAmount() {
            Order order = new Order();

            InvalidOrderingException ex = assertThrows(InvalidOrderingException.class,
                    () -> order.changeAmountTo(0));

            assertAll(
                    () -> assertEquals("Order amount must not be less than 1", ex.getMessage()),
                    () -> assertEquals(0, order.amount())
            );
        }

        @Test
        void shouldRejectNegativeAmount() {
            Order order = new Order();

            InvalidOrderingException ex = assertThrows(InvalidOrderingException.class,
                    () -> order.changeAmountTo(-1));

            assertAll(
                    () -> assertEquals("Order amount must not be less than 1", ex.getMessage()),
                    () -> assertEquals(0, order.amount())
            );
        }
    }

    @Nested
    class Approve {

        @Test
        void shouldApprovePendingOrder() {
            Order order = new Order();

            order.approve();

            assertEquals(State.APPROVED, order.state());
        }

        @Test
        void shouldRejectApprovingApprovedOrder() {
            Order order = new Order(1L, 2L, 3, LocalDateTime.now(), State.APPROVED);

            assertThrows(InvalidOrderingException.class, order::approve);

            assertEquals(State.APPROVED, order.state());
        }

        @Test
        void shouldRejectApprovingRejectedOrder() {
            Order order = new Order(1L, 2L, 3, LocalDateTime.now(), State.REJECTED);

            assertThrows(InvalidOrderingException.class, order::approve);

            assertEquals(State.REJECTED, order.state());
        }
    }

    @Nested
    class Reject {

        @Test
        void shouldRejectPendingOrder() {
            Order order = new Order();

            order.reject();

            assertEquals(State.REJECTED, order.state());
        }

        @Test
        void shouldRejectRejectingApprovedOrder() {
            Order order = new Order(1L, 2L, 3, LocalDateTime.now(), State.APPROVED);

            assertThrows(InvalidOrderingException.class, order::reject);

            assertEquals(State.APPROVED, order.state());
        }

        @Test
        void shouldRejectRejectingRejectedOrder() {
            Order order = new Order(1L, 2L, 3, LocalDateTime.now(), State.REJECTED);

            assertThrows(InvalidOrderingException.class, order::reject);

            assertEquals(State.REJECTED, order.state());
        }
    }

    @Nested
    class Builder {

        @Test
        void shouldBuildPendingOrder() {
            LocalDateTime beforeBuild = LocalDateTime.now();

            Order order = new Order.Builder()
                    .id(1L)
                    .bookId(2L)
                    .amount(3)
                    .build();

            LocalDateTime afterBuild = LocalDateTime.now();
            assertAll(
                    () -> assertEquals(1L, order.id()),
                    () -> assertEquals(2L, order.bookId()),
                    () -> assertEquals(3, order.amount()),
                    () -> assertEquals(State.APPROVAL_PENDING, order.state()),
                    () -> assertNotNull(order.createdAt()),
                    () -> assertCreatedBetween(order.createdAt(), beforeBuild, afterBuild)
            );
        }

        @Test
        void shouldRejectInvalidAmount() {
            Order.Builder builder = new Order.Builder();

            InvalidOrderingException ex = assertThrows(InvalidOrderingException.class,
                    () -> builder.amount(0));

            assertEquals("Order amount must not be less than 1", ex.getMessage());
        }
    }

    private static void assertCreatedBetween(LocalDateTime actual, LocalDateTime lowerBound, LocalDateTime upperBound) {
        boolean withinRange = !actual.isBefore(lowerBound) && !actual.isAfter(upperBound);
        assertTrue(withinRange);
    }
}
