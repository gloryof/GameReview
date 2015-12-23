WITH
    auth_cnf_change AS (
        SELECT
            *
        FROM
            user_authority
        WHERE
            authority_id = 1
    ),
    auth_review_post AS (
        SELECT
            *
        FROM
            user_authority
        WHERE
            authority_id = 2
    )
SELECT
    usr.user_id,
    usr.user_name,
    usr.lock_update_timestamp,
    account.login_id,
    account.password,
    CASE
        WHEN auth_cnf_change.authority_id IS NOT NULL THEN true
        ELSE false
    END AS conf_change,
    CASE
        WHEN auth_review_post.authority_id IS NOT NULL THEN true
        ELSE false
    END AS review_post
FROM
    user_info AS usr
    INNER JOIN user_account AS account ON
        usr.user_id = account.user_id
    LEFT JOIN auth_cnf_change ON
        auth_cnf_change.user_id = usr.user_id
    LEFT JOIN auth_review_post ON
        auth_review_post.user_id = usr.user_id
WHERE
    /*%if condition.loginId != null && condition.loginId.isEmpty() != true */
        account.login_id LIKE /* @prefix(condition.loginId) */'test%'
    /*%end */
    /*%if condition.userName != null && condition.userName.isEmpty() != true */
        AND usr.user_name LIKE /* @prefix(condition.userName) */'シュンツ%'
    /*%end */
ORDER BY
    usr.user_id ASC
/*%if condition.limits.activeLimit == true */
    LIMIT /* condition.limits.limit */10
    OFFSET /* condition.limits.offset */10
/*%end */
;