package jp.ne.glory.domain.review.value.search;

import jp.ne.glory.domain.game.entity.Game;
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
     * コンストラクタ.
     *
     * @param paramReview レビュー
     * @param paramGame ゲーム
     */
    public ReviewSearchResult(final Review paramReview, final Game paramGame) {

        review = paramReview;
        game = paramGame;
    }
}
