CREATE TABLE IF NOT EXISTS stats
(
    id    bigint                  NOT NULL GENERATED ALWAYS AS IDENTITY,
    app          character varying(250)  NOT NULL,
    uri          character varying(1000) NOT NULL,
    ip           character varying(15)   NOT NULL,
    timestamp timestamp
);