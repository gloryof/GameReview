package jp.ne.glory.infra.db.review.entity;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/**
 * レビュー点数.
 *
 * @author Junki Yamada
 */
@Entity
public class ReviewScore {

    /**
     * レビューID.
     */
    @Getter
    @Setter
    @Id
    private long reviewId;

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
