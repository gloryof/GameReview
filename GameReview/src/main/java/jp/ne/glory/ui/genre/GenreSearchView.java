package jp.ne.glory.ui.genre;

import java.util.List;
import java.util.stream.Collectors;
import jp.ne.glory.domain.genre.entity.Genre;

/**
 * ジャンル検索ビュー.
 *
 * @author Junki Yamada
 */
public class GenreSearchView {

    /**
     * ジャンルリスト.
     */
    public final List<GenreBean> genres;

    /**
     * コンストラクタ.
     *
     * @param genreList ジャンルリスト
     */
    public GenreSearchView(final List<Genre> genreList) {

        genres = genreList.stream().map(GenreBean::new).collect(Collectors.toList());
    }
}
