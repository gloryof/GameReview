package jp.ne.glory.infra.db.common.entity;

import jp.ne.glory.domain.common.value.SearchCondition;
import lombok.Getter;

/**
 * レコードの取得件数制限.
 *
 * @author Junki Yamada
 */
public class RecordLimits {

    /**
     * LIMIT件数.
     */
    @Getter
    private final int limit;

    /**
     * OFFSET件数.
     */
    @Getter
    private final int offset;

    /**
     * 件数制限が有効かのフラグ.
     */
    @Getter
    private final boolean activeLimit;

    /**
     * コンストラクタ.
     *
     * @param condition 検索条件
     */
    public RecordLimits(final SearchCondition condition) {

        this.activeLimit = (0 < condition.getTargetCount());
        if (this.isActiveLimit()) {

            this.limit = condition.getTargetCount();

            final int lpc = condition.getLotPerCount();
            final int ln = condition.getLotNumber();
            this.offset = lpc * (ln - 1);
        } else {

            this.limit = 0;
            this.offset = 0;
        }
    }
}
