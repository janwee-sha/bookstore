package com.janwee.bookstore.order.southbound.adapter.persistence;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@StaticMetamodel(TicketPO.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class TicketPO_ {

	public static final String CREATED_AT = "createdAt";
	public static final String ORDER_ID = "orderId";
	public static final String ID = "id";
	public static final String BOOK_ID = "bookId";

	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.TicketPO#createdAt
	 **/
	public static volatile SingularAttribute<TicketPO, LocalDateTime> createdAt;
	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.TicketPO#orderId
	 **/
	public static volatile SingularAttribute<TicketPO, Long> orderId;
	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.TicketPO#id
	 **/
	public static volatile SingularAttribute<TicketPO, Long> id;
	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.TicketPO
	 **/
	public static volatile EntityType<TicketPO> class_;
	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.TicketPO#bookId
	 **/
	public static volatile SingularAttribute<TicketPO, Long> bookId;

}

