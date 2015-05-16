package jp.ne.glory.domain.genre.value.search;

import java.util.List;
import jp.ne.glory.domain.common.value.SearchResults;
import jp.ne.glory.domain.genre.entity.Genre;

/**
 * ジャンル検索結果.
 *
 * @author Junki Yamada
 */
public class GenreSearchResults extends SearchResults<GenreSearchCondition, Genre> {

    /**
     * コンストラクタ.
     *
     * @param condition 検索条件
     * @param genreList ジャンルリスト
     * @param allCount 全件件数
     */
    public GenreSearchResults(final GenreSearchCondition condition, final List<Genre> genreList, final int allCount) {

        super(condition, genreList, allCount);
    }
}
