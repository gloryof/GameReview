package jp.ne.glory.ui.genre;

import jp.ne.glory.ui.review.ReviewListView;

/**
 * ジャンル検索結果画面.
 *
 * @author Junki Yamada
 */
public class GenreSearchResultView {

    /**
     * ジャンル検索.
     */
    public final GenreSearchView genreSearch;

    /**
     * レビュー.
     */
    public final ReviewListView reviewList;

    /**
     * コンストラクタ.
     *
     * @param genreSearch ジャンル検索
     * @param reviewList レビューリスト
     */
    public GenreSearchResultView(final GenreSearchView genreSearch, final ReviewListView reviewList) {

        this.genreSearch = genreSearch;
        this.reviewList = reviewList;
    }
}
