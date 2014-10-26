package jp.ne.glory.ui.top;

import jp.ne.glory.ui.genre.GenreSearchView;

/**
 * トップ画面情報.
 *
 * @author Junki Yamada
 */
public class TopView {

    /**
     * ジャンル検索.
     */
    public final GenreSearchView genreSearch;

    /**
     * コンストラクタ.
     *
     * @param genreSearch ジャンル検索
     */
    public TopView(final GenreSearchView genreSearch) {

        this.genreSearch = genreSearch;
    }
}
