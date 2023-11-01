DROP SCHEMA IF EXISTS "order" CASCADE;
CREATE SCHEMA "order";

ALTER SCHEMA "order" OWNER to janwee;

DROP TABLE IF EXISTS "order"."tbl_order";

CREATE TABLE "order"."tbl_order"
(
    id bigserial NOT NULL,
    book_id   char(64)              NOT NULL,
    "amount"  integer               NOT NULL,
    "state"   character varying(64) NOT NULL,
    create_by timestamp             NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "order"."tbl_order"
    OWNER to janwee;

DROP TABLE IF EXISTS "order"."tbl_ticket";

CREATE TABLE "order"."tbl_ticket"
(
    id bigserial NOT NULL,
    "order_id"  char(64)  NOT NULL,
    "book_id"   char(64)  NOT NULL,
    "create_by" timestamp NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "order"."tbl_ticket"
    OWNER to janwee;