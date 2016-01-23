package jp.ne.glory.ui.review;

import java.util.Arrays;
import java.util.List;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
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
    private final ScorePoint addictionScore;

    /**
     * ストーリー.
     */
    @Getter
    private final ScorePoint storyScore;

    /**
     * 操作性.
     */
    @Getter
    private final ScorePoint operabilityScore;

    /**
     * ロード時間.
     */
    @Getter
    private final ScorePoint loadTimeScore;

    /**
     * 音楽
     */
    @Getter
    private final ScorePoint musicScore;

    /**
     * 良い点.
     */
    @Getter
    private final List<String> goodPoint;

    /**
     * 悪い点.
     */
    @Getter
    private final List<String> badPoint;

    /**
     * コメント.
     */
    @Getter
    private final List<String> comment;

    /**
     * 投稿時刻.
     */
    @Getter
    private final DateTimeValue postDateTime;

    /**
     * 最終更新日時.s
     */
    @Getter
    private final DateTimeValue lastUpdateDateTime;

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
        addictionScore = score.getAddiction();
        storyScore = score.getStory();
        loadTimeScore = score.getLoadTime();
        operabilityScore = score.getOperability();
        musicScore = score.getMusic();

        final String line = System.getProperty("line.separator");
        goodPoint = Arrays.asList(review.getGoodPoint().getValue().split(line));
        badPoint = Arrays.asList(review.getBadPoint().getValue().split(line));
        comment = Arrays.asList(review.getComment().getValue().split(line));

        postDateTime = review.getPostTime().getValue();
        lastUpdateDateTime = review.getLastUpdate().getValue();
    }
}
