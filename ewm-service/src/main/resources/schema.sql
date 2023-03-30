CREATE TABLE IF NOT EXISTS users
(
    id_user    bigint                 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name  character varying(250) NOT NULL,
    email character varying(250) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id_user),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id_category  bigint                 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(250) NOT NULL,
    CONSTRAINT categories_pkey PRIMARY KEY (id_category),
    CONSTRAINT uq_categories_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id_location  bigint                 NOT NULL GENERATED ALWAYS AS IDENTITY,
    lat NUMERIC(9, 6) NOT NULL,
    lon NUMERIC(9, 6) NOT NULL,
    CONSTRAINT locations_pkey PRIMARY KEY (id_location)
);

CREATE TABLE IF NOT EXISTS states
(
    id_state   bigint                 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(9) NOT NULL,
    CONSTRAINT states_pkey PRIMARY KEY (id_state),
    CONSTRAINT uq_states_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id_event bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    title character varying(500) NOT NULL NOT NULL,
    description character varying(5000),
    annotation character varying(2000) NOT NULL,
    category_id bigint NOT NULL NOT NULL,
    initiator_id bigint NOT NULL NOT NULL,
    location_id bigint NOT NULL NOT NULL,
    state_id bigint NOT NULL,
    created_on timestamp without time zone,
    event_date timestamp without time zone NOT NULL,
    published_on timestamp without time zone,
    paid boolean NOT NULL,
    confirmed_requests bigint,
    participant_limit integer,
    views bigint,
    request_moderation boolean,
    CONSTRAINT events_pkey PRIMARY KEY (id_event),
    CONSTRAINT category_id_id_category FOREIGN KEY (category_id)
        REFERENCES categories (id_category),
    CONSTRAINT initiator_id_id_user FOREIGN KEY (initiator_id)
        REFERENCES users (id_user),
    CONSTRAINT location_id_id_location FOREIGN KEY (location_id)
        REFERENCES locations (id_location),
    CONSTRAINT state_id_id_state FOREIGN KEY (state_id)
        REFERENCES states (id_state)
)