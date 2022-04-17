CREATE TABLE author.tbl_author
(
    id bigint NOT NULL,
    author_name character varying(255) NOT NULL,
    profile character varying(1020) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS author.tbl_author
    OWNER to janwee;