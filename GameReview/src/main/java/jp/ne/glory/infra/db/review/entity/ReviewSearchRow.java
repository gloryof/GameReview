package jp.ne.glory.infra.db.review.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;

/**
 * レビュー検索結果.
 *
 * @author Junki Yamada
 */
@Entity
public class ReviewSearchRow {

    /**
     * レビューID.
     */
    @Getter
    @Setter
    private long reviewId;

    /**
     * ゲームID.
     */
    @Getter
    @Setter
    private long gameId;

    /**
     * ジャンルID.
     */
    @Getter
    @Setter
    private long genreId;

    /**
     * CERO-ID.
     */
    @Getter
    @Setter
    private long ceroId;

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
     * 投稿日時.
     */
    @Getter
    @Setter
    private LocalDateTime postTime;

    /**
     * 最終更新日時.
     */
    @Getter
    @Setter
    private LocalDateTime lastUpdate;

    /**
     * タイトル.
     */
    @Getter
    @Setter
    private String title;

    /**
     * 公式サイトのURL.
     */
    @Getter
    @Setter
    private String siteUrl;

    /**
     * ジャンル名.
     */
    @Getter
    @Setter
    private String genreName;

    /**
     * 熱中度
     */
    @Getter
    @Setter
    private int addiction;

    /**
     * ストーリー.
     */
    @Getter
    @Setter
    private int story;

    /**
     * 操作性.
     */
    @Getter
    @Setter
    private int operability;

    /**
     * ロード時間.
     */
    @Getter
    @Setter
    private int loadTime;

    /**
     * 音楽
     */
    @Getter
    @Setter
    private int music;
}
