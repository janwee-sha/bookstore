package unit.com.janwee.bookstore.order;

import com.janwee.bookstore.order.domain.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderUnitTest {
    @Test
    void createOrderWithNonPositiveAmountShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Order.create().ofAmount(0));
        assertThrows(IllegalArgumentException.class, () -> Order.create().ofAmount(-1));
    }
}
