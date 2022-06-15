DROP SCHEMA IF EXISTS "order" CASCADE;
CREATE SCHEMA "order";

ALTER SCHEMA "order"
    OWNER to janwee;

DROP TABLE IF EXISTS "order"."tbl_order";

CREATE TABLE "order"."tbl_order"
(
    id char(32) NOT NULL,
    create_by timestamp NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS "order"."tbl_order"
    OWNER to janwee;