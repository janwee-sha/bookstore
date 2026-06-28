package com.janwee.bookstore.order.northbound.remote.subscriber;

import com.janwee.bookstore.order.northbound.local.OrderApplicationService;
import com.janwee.bookstore.order.northbound.message.BookOrdered;
import com.janwee.bookstore.order.northbound.message.BookSoldOut;
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
    void shouldDelegateBookOrderedConsumerToApplicationService() {
        BookOrdered event = new BookOrdered(1L, 2L);

        subscriber.bookOrderedConsumer().accept(event);

        verify(orderAppService).approve(1L);
    }

    @Test
    void shouldDelegateBookSoldOutConsumerToApplicationService() {
        BookSoldOut event = new BookSoldOut(1L, 2L);

        subscriber.bookSoldOutConsumer().accept(event);

        verify(orderAppService).reject(1L);
    }
}
