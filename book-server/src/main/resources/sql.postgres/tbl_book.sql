CREATE TABLE author.tbl_book
(
    id bigserial NOT NULL,
    book_name character varying(255) NOT NULL,
    publish_by date NOT NULL,
    publisher character varying(255) NOT NULL,
    author_id bigint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS author.tbl_book
    OWNER to janwee;