DROP SCHEMA IF EXISTS "author" CASCADE;
CREATE SCHEMA "author";

DROP TABLE IF EXISTS "author"."authors";
CREATE TABLE "author"."authors"
(
    id          BIGINT       NOT NULL,
    author_name VARCHAR(255) NOT NULL,
    profile     VARCHAR(255) NOT NULL,
    phone_no    VARCHAR(255),
    CONSTRAINT pk_authors PRIMARY KEY (id)
);