CREATE TABLE mst_genre (
    genre_id bigint NOT NULL,
    genre_name varchar(50) NOT NULL,
    CONSTRAINT pk_mst_genre PRIMARY KEY(genre_id)
);

CREATE SEQUENCE seq_genre_id;