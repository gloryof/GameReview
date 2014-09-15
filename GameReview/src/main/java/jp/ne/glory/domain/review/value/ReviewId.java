package jp.ne.glory.domain.review.value;

import jp.ne.glory.domain.common.type.EntityId;

/**
 * レビューID.
 * @author Junki Yamada
 */
public class ReviewId extends EntityId {
    
    /**
     * 値を設定する
     * @param paramValue 値 
     */
    public ReviewId(final Long paramValue) {

        super(paramValue);
    }

    /**
     * 未採番のレビューIDを取得する.
     * @return レビューID
     */
    public static ReviewId notNumberingValue() {

        return new ReviewId(null);
    }
}
