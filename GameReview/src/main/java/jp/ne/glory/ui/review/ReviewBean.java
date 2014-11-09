package jp.ne.glory.ui.review;

import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.review.entity.Review;

/**
 * レビュー画面情報.
 *
 * @author Junki Yamada
 */
public class ReviewBean {

    /**
     * タイトル.
     */
    public final String title;

    /**
     * 良い点.
     */
    public final String goodPoint;

    /**
     * 悪い点.
     */
    public final String badPoint;

    /**
     * コメント.
     */
    public final String comment;

    /**
     * コンストラクタ.
     *
     * @param review レビュー
     * @param game ゲーム
     */
    public ReviewBean(final Review review, final Game game) {

        title = game.title.value;
        goodPoint = review.gooodPoint.value;
        badPoint = review.badPoint.value;
        comment = review.comment.value;
    }
}
