CREATE SCHEMA IF NOT EXISTS "order";

CREATE SEQUENCE IF NOT EXISTS "order".seq_orders INCREMENT BY 1 START WITH 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE SEQUENCE IF NOT EXISTS "order".seq_tickets INCREMENT BY 1 START WITH 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE TABLE IF NOT EXISTS "order"."orders"
(
    id         BIGINT NOT NULL DEFAULT nextval('"order".seq_orders'::regclass),
    book_id    BIGINT NOT NULL,
    amount     INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    state      VARCHAR(255),
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "order"."tickets"
(
    id           BIGINT    NOT NULL DEFAULT nextval('"order".seq_tickets'::regclass),
    "order_id"   BIGINT    NOT NULL,
    "book_id"    BIGINT    NOT NULL,
    "created_at" timestamp NOT NULL,
    PRIMARY KEY (id)
);