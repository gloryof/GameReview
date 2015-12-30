package jp.ne.glory.infra.db.review.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/**
 * レビュー情報テーブル.
 *
 * @author Junki Yamada
 */
@Entity
public class ReviewBaseInfo {

    /**
     * レビューID.
     */
    @Getter
    @Setter
    @Id
    private long reviewId;

    /**
     * ゲームID.
     */
    @Getter
    @Setter
    private long gameId;
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
     * ロック管理タイムスタンプ.
     */
    @Getter
    @Setter
    private LocalDateTime lockUpdateTimestamp;
}
