CREATE SCHEMA IF NOT EXISTS "order";

ALTER SCHEMA IF EXISTS "order" OWNER to janwee;

CREATE TABLE IF NOT EXISTS "order"."orders"
(
    id        bigserial NOT NULL,
    book_id   BIGINT    NOT NULL,
    amount    INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    state     VARCHAR(255),
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "order"."orders"
    OWNER to janwee;

CREATE TABLE IF NOT EXISTS "order"."tickets"
(
    id          bigserial NOT NULL,
    "order_id"  BIGINT    NOT NULL,
    "book_id"   BIGINT    NOT NULL,
    "created_at" timestamp NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "order"."tickets"
    OWNER to janwee;