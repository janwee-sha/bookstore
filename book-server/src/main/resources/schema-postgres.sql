DROP SCHEMA IF EXISTS "book" CASCADE;
CREATE SCHEMA "book";

ALTER SCHEMA "book" OWNER to janwee;

DROP TABLE IF EXISTS "book"."tbl_book";

CREATE TABLE "book"."tbl_book"
(
    id bigserial NOT NULL,
    book_name  character varying(255) NOT NULL,
    amount int4 NOT NULL,
    price      numeric(102, 2)        NOT NULL,
    publish_by date                   NOT NULL,
    publisher  character varying(255) NOT NULL,
    author_id  bigint                 NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "book"."tbl_book"
    OWNER to janwee;

DROP TABLE IF EXISTS "book"."tbl_ticket";

CREATE TABLE "book"."tbl_ticket"
(
    id bigserial NOT NULL,
    "order_id"  char(64)  NOT NULL,
    "book_id"   char(64)  NOT NULL,
    "create_by" timestamp NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "book"."tbl_tel_slice"
    OWNER to janwee;