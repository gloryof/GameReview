CREATE TABLE user_info
(
  user_id bigint NOT NULL,
  user_name character varying(50) NOT NULL,
  lock_update_timestamp timestamp with time zone,
  CONSTRAINT pk_user PRIMARY KEY (user_id)
)
CREATE SEQUENCE seq_user_id;
