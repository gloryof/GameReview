SELECT
    base.review_id,
    base.game_id,
    base.good_point,
    base.bad_point,
    base.comment,
    base.post_time,
    base.last_update,
    base.lock_update_timestamp,
    score.addiction,
    score.story,
    score.operability,
    score.load_time,
    score.music
FROM
    review_base_info AS base
    INNER JOIN review_score AS score ON
        base.review_id = score.review_id
WHERE
    base.review_id = /*reviewId*/1
