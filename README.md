# book-store

Application Choreography Saga:

1. Order Service: Create an order -> [OrderCreated]
2. [OrderCreated] -> Book Service: Ticket{orderId,bookId,createBy,state(ORDER_PENDING)} -> [TicketCreated]
3. [TicketCreated] -> 


