DROP SCHEMA IF EXISTS "book" CASCADE;
CREATE SCHEMA "book";

DROP TABLE IF EXISTS "book"."books";
CREATE TABLE "book"."books"
(
    id           bigserial    NOT NULL,
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
    id       bigserial    NOT NULL,
    name     VARCHAR(255) NOT NULL,
    profile  VARCHAR(255) NOT NULL,
    phone_no VARCHAR(255),
    CONSTRAINT pk_authors PRIMARY KEY (id)
);