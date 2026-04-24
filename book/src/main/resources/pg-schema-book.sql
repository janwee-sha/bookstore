CREATE SCHEMA IF NOT EXISTS "book";

CREATE SEQUENCE IF NOT EXISTS "book".seq_books INCREMENT BY 1 START WITH 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE SEQUENCE IF NOT EXISTS "book".seq_authors INCREMENT BY 1 START WITH 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE TABLE IF NOT EXISTS "book"."books"
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

CREATE TABLE IF NOT EXISTS "book"."authors"
(
    id       BIGINT       NOT NULL DEFAULT nextval('"book".seq_authors'::regclass),
    name     VARCHAR(255) NOT NULL,
    profile  VARCHAR(255) NOT NULL,
    phone_no VARCHAR(255),
    CONSTRAINT pk_authors PRIMARY KEY (id)
);