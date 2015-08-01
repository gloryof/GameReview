package jp.ne.glory.ui.admin.review;

import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.ui.admin.game.GameBean;
import lombok.Getter;
import lombok.Setter;

/**
 * レビュー情報Bean.
 *
 * @author Junki Yamada
 */
public class ReviewBean {

    /**
     * レビューID.
     */
    @Getter
    @Setter
    private long reviewId;

    /**
     * 良い点.
     */
    @Getter
    @Setter
    private String goodPoint;

    /**
     * 悪い点.
     */
    @Getter
    @Setter
    private String badPoint;

    /**
     * コメント.
     */
    @Getter
    @Setter
    private String comment;

    /**
     * 熱中度.
     */
    @Getter
    @Setter
    private ScorePoint addiction = ScorePoint.NotInput;

    /**
     * ストーリー.
     */
    @Getter
    @Setter
    private ScorePoint story = ScorePoint.NotInput;

    /**
     * 操作性.
     */
    @Getter
    @Setter
    private ScorePoint operability = ScorePoint.NotInput;

    /**
     * ロード時間.
     */
    @Getter
    @Setter
    private ScorePoint loadTime = ScorePoint.NotInput;

    /**
     * 音楽
     */
    @Getter
    @Setter
    private ScorePoint music = ScorePoint.NotInput;

    /**
     * 投稿時刻.
     */
    @Getter
    @Setter
    private DateTimeValue postDatetime;

    /**
     * ゲーム情報.
     */
    @Getter
    @Setter
    private GameBean game;

    /**
     * コンストラクタ.
     */
    public ReviewBean() {

        super();
    }

    /**
     * コンストラクタ.
     *
     * @param result 検索結果
     */
    public ReviewBean(final ReviewSearchResult result) {

        final Review review = result.getReview();
        final Score score = review.getScore();

        reviewId = review.getId().getValue();
        goodPoint = review.getGoodPoint().getValue();
        badPoint = review.getBadPoint().getValue();
        comment = review.getComment().getValue();

        addiction = score.getAddiction();
        story = score.getStory();
        operability = score.getOperability();
        loadTime = score.getLoadTime();
        music = score.getMusic();

        postDatetime = review.getPostTime().getValue();

        this.game = new GameBean(result.getGame(), result.getGenre());
    }
}
