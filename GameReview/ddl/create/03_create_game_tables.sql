CREATE TABLE game (
    game_id bigint NOT NULL,
    title varchar(100) NOT NULL,
    genre_id bigint NOT NULL,
    cero_id bigint NOT NULL,
    CONSTRAINT pk_game PRIMARY KEY(game_id)
);

CREATE TABLE game_site_url (
    game_id bigint NOT NULL,
    site_url varchar(2083) NOT NULL,
    CONSTRAINT pk_game_site_url PRIMARY KEY(game_id)
);

CREATE SEQUENCE seq_game_id;