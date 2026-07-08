package com.janwee.bookstore.order.northbound.remote.subscriber;

import com.janwee.bookstore.order.northbound.local.OrderApplicationService;
import com.janwee.bookstore.order.northbound.message.StockReservationConfirmed;
import com.janwee.bookstore.order.northbound.message.StockReservationRejected;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitBookEventSubscriberUnitTest {
    @Mock
    private OrderApplicationService orderAppService;

    @InjectMocks
    private RabbitBookEventSubscriber subscriber;

    @Test
    void shouldDelegateStockReservationConfirmedConsumerToApplicationService() {
        StockReservationConfirmed event = new StockReservationConfirmed(1L, 2L);

        subscriber.stockReservationConfirmedConsumer().accept(event);

        verify(orderAppService).approve(1L);
    }

    @Test
    void shouldDelegateStockReservationRejectedConsumerToApplicationService() {
        StockReservationRejected event = new StockReservationRejected(1L, 2L);

        subscriber.stockReservationRejectedConsumer().accept(event);

        verify(orderAppService).reject(1L);
    }
}
