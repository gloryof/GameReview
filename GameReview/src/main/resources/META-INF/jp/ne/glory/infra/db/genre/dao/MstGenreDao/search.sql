SELECT
    /*%expand*/*
FROM
    mst_genre
WHERE
    genre_name LIKE /* @prefix(condition.genreName) */'ジャンル';
