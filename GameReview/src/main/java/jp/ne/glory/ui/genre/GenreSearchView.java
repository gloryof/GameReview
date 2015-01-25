package jp.ne.glory.ui.genre;

import java.util.List;
import java.util.stream.Collectors;
import jp.ne.glory.domain.genre.entity.Genre;
import lombok.Getter;

/**
 * ジャンル検索ビュー.
 *
 * @author Junki Yamada
 */
public class GenreSearchView {

    /**
     * ジャンルリスト.
     */
    @Getter
    private final List<GenreBean> genres;

    /**
     * コンストラクタ.
     *
     * @param genreList ジャンルリスト
     */
    public GenreSearchView(final List<Genre> genreList) {

        genres = genreList.stream().map(GenreBean::new).collect(Collectors.toList());
    }
}
