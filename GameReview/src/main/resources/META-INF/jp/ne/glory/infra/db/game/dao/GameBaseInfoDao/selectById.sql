SELECT
    base.game_id,
    base.title,
    base.genre_id,
    base.cero_id,
    site.site_url
FROM
    game_base_info AS base
    LEFT JOIN game_site_url AS site ON
        base.game_id = site.game_id
WHERE
    base.game_id = /* gameId */1
ORDER BY
    game_id ASC