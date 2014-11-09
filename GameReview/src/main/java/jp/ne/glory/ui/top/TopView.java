package jp.ne.glory.ui.top;

import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewView;

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
     * レビュー.
     */
    public final ReviewView review;

    /**
     * コンストラクタ.
     *
     * @param genreSearch ジャンル検索
     * @param review レビュー
     */
    public TopView(final GenreSearchView genreSearch, final ReviewView review) {

        this.genreSearch = genreSearch;
        this.review = review;
    }
}
