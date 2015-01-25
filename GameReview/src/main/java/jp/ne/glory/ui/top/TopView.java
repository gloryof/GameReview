package jp.ne.glory.ui.top;

import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewView;
import lombok.Getter;

/**
 * トップ画面情報.
 *
 * @author Junki Yamada
 */
public class TopView {

    /**
     * ジャンル検索.
     */
    @Getter
    private final GenreSearchView genreSearch;

    /**
     * レビュー.
     */
    @Getter
    private final ReviewView review;

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
