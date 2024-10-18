DROP SCHEMA IF EXISTS "order" CASCADE;
CREATE SCHEMA "order";

ALTER SCHEMA "order" OWNER to janwee;

DROP TABLE IF EXISTS "order"."orders";

CREATE TABLE "order"."orders"
(
    id        bigserial NOT NULL,
    book_id   BIGINT    NOT NULL,
    amount    INTEGER,
    create_by TIMESTAMP WITHOUT TIME ZONE,
    state     SMALLINT,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "order"."orders"
    OWNER to janwee;

DROP TABLE IF EXISTS "order"."tickets";

CREATE TABLE "order"."tickets"
(
    id          bigserial NOT NULL,
    "order_id"  BIGINT    NOT NULL,
    "book_id"   BIGINT    NOT NULL,
    "create_by" timestamp NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "order"."tickets"
    OWNER to janwee;