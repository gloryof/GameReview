CREATE TABLE review_base_info (
    review_id bigint NOT NULL,
    game_id bigint NOT NULL,
    good_point text NOT NULL,
    bad_point text NOT NULL,
    comment text NOT NULL,
    post_time timestamp with time zone NOT NULL,
    last_update timestamp with time zone NOT NULL,
    lock_update_timestamp timestamp with time zone NOT NULL,
    CONSTRAINT pk_review PRIMARY KEY(review_id)
);

CREATE TABLE review_score (
    review_id bigint NOT NULL,
    addiction integer NOT NULL,
    story integer NOT NULL,
    operability integer NOT NULL,
    load_time integer NOT NULL,
    music integer NOT NULL,
    CONSTRAINT pk_review_score PRIMARY KEY(review_id)
);

CREATE SEQUENCE seq_review_id;