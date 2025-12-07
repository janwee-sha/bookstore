DROP SCHEMA IF EXISTS "book" CASCADE;
CREATE SCHEMA "book";

DROP SEQUENCE IF EXISTS "book".seq_books;
CREATE SEQUENCE "book".seq_books INCREMENT BY 1 START WITH 1 NO MINVALUE NO MAXVALUE CACHE 1;

DROP SEQUENCE IF EXISTS "book".seq_authors;
CREATE SEQUENCE "book".seq_authors INCREMENT BY 1 START WITH 1 NO MINVALUE NO MAXVALUE CACHE 1;

DROP TABLE IF EXISTS "book"."books";
CREATE TABLE "book"."books"
(
    id           BIGINT       NOT NULL DEFAULT nextval('"book".seq_books'::regclass),
    name         VARCHAR(255) NOT NULL,
    amount       INTEGER      NOT NULL,
    price        VARCHAR(255) NOT NULL,
    published_at date         NOT NULL,
    publisher    VARCHAR(255) NOT NULL,
    author_id    BIGINT       NOT NULL,
    CONSTRAINT pk_books PRIMARY KEY (id)
);

DROP TABLE IF EXISTS "book"."authors";
CREATE TABLE "book"."authors"
(
    id       BIGINT       NOT NULL DEFAULT nextval('"book".seq_authors'::regclass),
    name     VARCHAR(255) NOT NULL,
    profile  VARCHAR(255) NOT NULL,
    phone_no VARCHAR(255),
    CONSTRAINT pk_authors PRIMARY KEY (id)
);