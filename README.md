# book-store

Application Choreography Saga:

1. Order Service: Create an order -> [OrderCreated]
2. [OrderCreated] -> Book Service: Ticket{orderId,bookId,createBy}, Book{amount=amount-orderAmount}-> 
[TicketCreated]/[OrderRejected]
3. [TicketCreated] -> Order Service: Order{state="CREATED"}/[OrderRejected] -> Order Service: Order{state="REJECTED"}


