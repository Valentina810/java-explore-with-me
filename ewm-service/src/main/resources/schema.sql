CREATE TABLE IF NOT EXISTS users
(
    id    bigint                  NOT NULL GENERATED ALWAYS AS IDENTITY,
    name          character varying(250)  NOT NULL,
    email   character varying(250) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);