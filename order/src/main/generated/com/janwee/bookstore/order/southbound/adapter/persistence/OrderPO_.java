package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.State;
import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@StaticMetamodel(OrderPO.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class OrderPO_ {

	public static final String CREATED_AT = "createdAt";
	public static final String AMOUNT = "amount";
	public static final String ID = "id";
	public static final String STATE = "state";
	public static final String BOOK_ID = "bookId";

	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.OrderPO#createdAt
	 **/
	public static volatile SingularAttribute<OrderPO, LocalDateTime> createdAt;
	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.OrderPO#amount
	 **/
	public static volatile SingularAttribute<OrderPO, Integer> amount;
	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.OrderPO#id
	 **/
	public static volatile SingularAttribute<OrderPO, Long> id;
	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.OrderPO#state
	 **/
	public static volatile SingularAttribute<OrderPO, State> state;
	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.OrderPO
	 **/
	public static volatile EntityType<OrderPO> class_;
	
	/**
	 * @see com.janwee.bookstore.order.southbound.adapter.persistence.OrderPO#bookId
	 **/
	public static volatile SingularAttribute<OrderPO, Long> bookId;

}

