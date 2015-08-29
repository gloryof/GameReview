package jp.ne.glory.ui.admin.review;

import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.review.entity.Review;
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
    @FormParam("reviewId")
    private Long reviewId;

    /**
     * 良い点.
     */
    @Getter
    @Setter
    @FormParam("goodPoint")
    private String goodPoint;

    /**
     * 悪い点.
     */
    @Getter
    @Setter
    @FormParam("badPoint")
    private String badPoint;

    /**
     * コメント.
     */
    @Getter
    @Setter
    @FormParam("comment")
    private String comment;

    /**
     * スコア.
     */
    @Getter
    @Setter
    @BeanParam
    private ScoreBean score;

    /**
     * 投稿時刻.
     */
    @Getter
    @Setter
    private DateTimeValue postDateTime;

    /**
     * 最終更新日時.s
     */
    @Getter
    @Setter
    private DateTimeValue lastUpdateDateTime;

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

        reviewId = review.getId().getValue();
        goodPoint = review.getGoodPoint().getValue();
        badPoint = review.getBadPoint().getValue();
        comment = review.getComment().getValue();
        score = new ScoreBean(review.getScore());
        postDateTime = review.getPostTime().getValue();
        lastUpdateDateTime = review.getLastUpdate().getValue();

        this.game = new GameBean(result.getGame(), result.getGenre());
    }
}
