CREATE SEQUENCE IF NOT EXISTS t_employee_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE t_employee
(
    id                     BIGINT       NOT NULL,
    first_name             VARCHAR(255) NOT NULL,
    middle_name            VARCHAR(255),
    last_name              VARCHAR(255) NOT NULL,
    badger_number          VARCHAR(255),
    social_security_number VARCHAR(255),
    nick_name              VARCHAR(255),
    employment_status      VARCHAR(255) NOT NULL,
    manager_id             BIGINT       NOT NULL,
    company_id             BIGINT,
    hired_since            date,
    phone                  VARCHAR(255) NOT NULL,
    fax                    VARCHAR(255) NOT NULL,
    country                VARCHAR(255) NOT NULL,
    state                  VARCHAR(255) NOT NULL,
    city                   VARCHAR(255) NOT NULL,
    street1                VARCHAR(255) NOT NULL,
    street2                VARCHAR(255) NOT NULL,
    zip                    VARCHAR(255),
    email                  VARCHAR(255),
    CONSTRAINT pk_t_employee PRIMARY KEY (id)
);

ALTER TABLE t_employee
    ADD CONSTRAINT FK_T_EMPLOYEE_ON_COMPANY FOREIGN KEY (company_id) REFERENCES t_company (id);

ALTER TABLE t_employee
    ADD CONSTRAINT FK_T_EMPLOYEE_ON_MANAGER FOREIGN KEY (manager_id) REFERENCES t_employee (id);