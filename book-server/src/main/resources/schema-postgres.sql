DROP SCHEMA IF EXISTS "book" CASCADE;
CREATE SCHEMA "book";

ALTER SCHEMA "book"
    OWNER to janwee;

DROP TABLE IF EXISTS "book"."tbl_book";

CREATE TABLE "book"."tbl_book"
(
    id bigserial NOT NULL,
    book_name character varying(255) NOT NULL,
    amount int4 NOT NULL,
    price numeric(102,2) NOT NULL,
    publish_by date NOT NULL,
    publisher character varying(255) NOT NULL,
    author_id bigint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "book"."tbl_book"
    OWNER to janwee;