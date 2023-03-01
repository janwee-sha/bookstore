DROP SCHEMA IF EXISTS "author" CASCADE;
CREATE SCHEMA "author";

ALTER SCHEMA "author" OWNER to janwee;

DROP TABLE IF EXISTS "author"."tbl_author";

CREATE TABLE IF NOT EXISTS "author"."tbl_author"
(
    id bigserial NOT NULL,
    author_name character varying(255) COLLATE pg_catalog."default"  NOT NULL,
    profile     character varying(1020) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tbl_author_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS "author"."tbl_author"
    OWNER to janwee;

DROP TABLE IF EXISTS "author"."tbl_tel_slice";
CREATE TABLE "author"."tbl_tel_slice"
(
    "id" bigserial NOT NULL,
    "user_id" int8 NOT NULL,
    "order" int2 NOT NULL,
    "value" char(64) COLLATE "pg_catalog"."default" NOT NULL,
    CONSTRAINT "tbl_tel_slice_pkey" PRIMARY KEY (id),
    CONSTRAINT "pk_uid_order" UNIQUE ("user_id", "order")
)
;

CREATE INDEX "idx_value" ON "author"."tbl_tel_slice" USING btree (
  "value" COLLATE "pg_catalog"."default" "pg_catalog"."bpchar_ops" ASC NULLS LAST
);

ALTER TABLE IF EXISTS "author"."tbl_tel_slice"
    OWNER to janwee;