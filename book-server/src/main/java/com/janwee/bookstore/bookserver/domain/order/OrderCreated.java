package com.janwee.bookstore.bookserver.domain.order;

import com.janwee.bookstore.common.domain.event.DomainEvent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCreated implements DomainEvent {
    private static final long serialVersionUID = 153249684583902383L;
    private String orderId;
    private String bookId;
    private LocalDateTime createBy;
}
