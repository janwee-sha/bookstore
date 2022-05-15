DROP SCHEMA IF EXISTS "author" CASCADE;
CREATE SCHEMA "author";

ALTER SCHEMA "author"
    OWNER to janwee;

DROP TABLE IF EXISTS "author"."tbl_author";

CREATE TABLE IF NOT EXISTS "author"."tbl_author"
(
    id bigint NOT NULL,
    author_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    profile character varying(1020) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tbl_author_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS "author"."tbl_author"
    OWNER to janwee;