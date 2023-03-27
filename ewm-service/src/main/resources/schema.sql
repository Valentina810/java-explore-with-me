CREATE TABLE IF NOT EXISTS users
(
    id    bigint                 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name  character varying(250) NOT NULL,
    email character varying(250) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   bigint                 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(250) NOT NULL,
    CONSTRAINT categories_pkey PRIMARY KEY (id),
    CONSTRAINT uq_categories_name UNIQUE (name)
);