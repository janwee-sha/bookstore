DROP SCHEMA IF EXISTS "order" CASCADE;
CREATE SCHEMA "order";

DROP SEQUENCE IF EXISTS "order".seq_orders;
CREATE SEQUENCE "order".seq_orders INCREMENT BY 1 START WITH 1 NO MINVALUE NO MAXVALUE CACHE 1;

DROP SEQUENCE IF EXISTS "order".seq_tickets;
CREATE SEQUENCE "order".seq_tickets INCREMENT BY 1 START WITH 1 NO MINVALUE NO MAXVALUE CACHE 1;

DROP TABLE IF EXISTS "order"."orders";
CREATE TABLE "order"."orders"
(
    id         BIGINT NOT NULL DEFAULT nextval('"order".seq_orders'::regclass),
    book_id    BIGINT NOT NULL,
    amount     INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    state      VARCHAR(255),
    CONSTRAINT pk_orders PRIMARY KEY (id)
);


DROP TABLE IF EXISTS "order"."tickets";
CREATE TABLE "order"."tickets"
(
    id           BIGINT    NOT NULL DEFAULT nextval('"order".seq_tickets'::regclass),
    "order_id"   BIGINT    NOT NULL,
    "book_id"    BIGINT    NOT NULL,
    "created_at" timestamp NOT NULL,
    PRIMARY KEY (id)
);