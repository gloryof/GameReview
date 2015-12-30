SELECT
    base.review_id,
    base.game_id,
    base.good_point,
    base.bad_point,
    base.comment,
    base.post_time,
    base.last_update,
    score.addiction,
    score.story,
    score.operability,
    score.load_time,
    score.music,
    game.title,
    site.site_url,
    genre.genre_name
FROM
    review_base_info AS base
    INNER JOIN review_score AS score ON
        base.review_id = score.review_id
    INNER JOIN game_base_info AS game ON
        base.game_id = game.game_id
    INNER JOIN game_site_url AS site ON
        game.game_id = site.game_id
    INNER JOIN mst_genre AS genre on
        game.genre_id = genre.genre_id
        /*%if condition.genreIds != null && condition.genreIds.isEmpty() == false*/
            AND genre.genre_id IN /*condition.genreIds*/(1,2,3)
        /*%end**/
WHERE
    /*%if condition.reviewIds != null && condition.reviewIds.isEmpty() == false*/
        base.review_id IN /*condition.reviewIds*/(1,2,3)
    /*%end*/
ORDER BY
    base.review_id DESC
/*%if condition.limits.activeLimit == true */
    LIMIT /* condition.limits.limit */10
    OFFSET /* condition.limits.offset */10
/*%end */

