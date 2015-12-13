INSERT INTO user_info VALUES (nextval('seq_user_id'), 'テストデータ', now());

INSERT INTO
    user_account
SELECT
    user_id,
    'test-user',
    '5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8' -- password
FROM
    user_info
LIMIT 1;

WITH
    target_user AS (
        SELECT
            user_id
        FROM
            user_info
        LIMIT
            1
    ),
    authority AS  (
        SELECT
            generate_series(1,2)
    )
INSERT INTO
    user_authority
SELECT
    *
FROM
    target_user, authority
;
