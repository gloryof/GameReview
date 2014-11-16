package jp.ne.glory.domain.review.value.search;

import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.entity.Review;

/**
 * レビュー検索結果.
 *
 * @author Junki Yamda
 */
public class ReviewSearchResult {

    /**
     * レビュー.
     */
    public final Review review;

    /**
     * ゲーム.
     */
    public final Game game;

    /**
     * ジャンル
     */
    public final Genre genre;

    /**
     * コンストラクタ.
     *
     * @param paramReview レビュー
     * @param paramGame ゲーム
     * @param paramGenre ジャンル
     */
    public ReviewSearchResult(final Review paramReview, final Game paramGame, final Genre paramGenre) {

        review = paramReview;
        game = paramGame;
        genre = paramGenre;
    }
}
