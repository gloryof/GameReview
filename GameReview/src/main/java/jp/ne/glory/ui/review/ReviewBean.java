package jp.ne.glory.ui.review;

import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.Score;

/**
 * レビュー画面情報.
 *
 * @author Junki Yamada
 */
public class ReviewBean {

    /**
     * レビューID
     */
    public final Long reviewId;

    /**
     * タイトル.
     */
    public final String title;

    /**
     * ジャンル名
     */
    public final String genreName;

    /**
     * 熱中度
     */
    public final String addictionScore;

    /**
     * ストーリー.
     */
    public final String storyScore;

    /**
     * 操作性.
     */
    public final String operabilityScore;

    /**
     * ロード時間.
     */
    public final String loadTimeScore;

    /**
     * 音楽
     */
    public final String musicScore;

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
     * @param genre ジャンル
     */
    public ReviewBean(final Review review, final Game game, final Genre genre) {

        reviewId = review.id.value;
        title = game.title.value;
        genreName = genre.name.value;

        final Score score = review.score;
        addictionScore = score.addiction.label;
        storyScore = score.story.label;
        loadTimeScore = score.loadTime.label;
        operabilityScore = score.operability.label;
        musicScore = score.music.label;

        goodPoint = review.gooodPoint.value;
        badPoint = review.badPoint.value;
        comment = review.comment.value;
    }
}
