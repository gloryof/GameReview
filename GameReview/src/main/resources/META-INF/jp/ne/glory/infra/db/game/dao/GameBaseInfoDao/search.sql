SELECT
    base.game_id,
    base.title,
    base.genre_id,
    base.cero_id,
    site.site_url
FROM
    (
        SELECT
            *
        FROM
            game_base_info
        WHERE
            /*%if condition.title != null && condition.title.isEmpty() != true */
                title LIKE /* @prefix(condition.title) */'test%'
            /*%end */
            /*%if condition.ceroRating != null  */
                AND cero_id = /* condition.ceroRating */1
            /*%end */
            /*%if condition.genreId != null  */
                AND genre_id = /* condition.genreId */1
            /*%end */
    ) base
    LEFT JOIN game_site_url AS site ON
        base.game_id = site.game_id
ORDER BY
    base.game_id ASC
/*%if condition.limits.activeLimit == true */
    LIMIT /* condition.limits.limit */10
    OFFSET /* condition.limits.offset */10
/*%end */