package jp.ne.glory.ui.review;

import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.Score;
import lombok.Getter;

/**
 * レビュー画面情報.
 *
 * @author Junki Yamada
 */
public class ReviewBean {

    /**
     * レビューID
     */
    @Getter
    private final Long reviewId;

    /**
     * タイトル.
     */
    @Getter
    private final String title;

    /**
     * ジャンル名
     */
    @Getter
    private final String genreName;

    /**
     * 熱中度
     */
    @Getter
    private final String addictionScore;

    /**
     * ストーリー.
     */
    @Getter
    private final String storyScore;

    /**
     * 操作性.
     */
    @Getter
    private final String operabilityScore;

    /**
     * ロード時間.
     */
    @Getter
    private final String loadTimeScore;

    /**
     * 音楽
     */
    @Getter
    private final String musicScore;

    /**
     * 良い点.
     */
    @Getter
    private final String goodPoint;

    /**
     * 悪い点.
     */
    @Getter
    private final String badPoint;

    /**
     * コメント.
     */
    @Getter
    private final String comment;

    /**
     * コンストラクタ.
     *
     * @param review レビュー
     * @param game ゲーム
     * @param genre ジャンル
     */
    public ReviewBean(final Review review, final Game game, final Genre genre) {

        reviewId = review.getId().getValue();
        title = game.getTitle().getValue();
        genreName = genre.getName().getValue();

        final Score score = review.getScore();
        addictionScore = score.getAddiction().label;
        storyScore = score.getStory().label;
        loadTimeScore = score.getLoadTime().label;
        operabilityScore = score.getOperability().label;
        musicScore = score.getMusic().label;

        goodPoint = review.getGoodPoint().getValue();
        badPoint = review.getBadPoint().getValue();
        comment = review.getComment().getValue();
    }
}
