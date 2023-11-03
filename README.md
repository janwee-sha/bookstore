# bookstore

Application Choreography Saga:

1. Order Service: insert Order{id,bookId,amount,createBy,state="CREATED"} -> [OrderCreated]
2. [OrderCreated] -> Book Service: insert Ticket{orderId,bookId,createBy}, update Book{amount=amount-orderAmount}-> 
[TicketCreated]/[OrderRejected]
3. [TicketCreated] -> Order Service: update Order{state="CREATED"}/[OrderRejected] -> Order Service: select update Order{state="REJECTED"}


