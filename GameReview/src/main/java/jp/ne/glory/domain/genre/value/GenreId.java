package jp.ne.glory.domain.genre.value;

import jp.ne.glory.domain.common.type.EntityId;

/**
 * ジャンルID
 * @author Junki Yamada
 */
public class GenreId extends EntityId {

    /**
     * 値を設定する
     * @param paramValue 値 
     */
    public GenreId(final Long paramValue) {

        super(paramValue);
    }

    /**
     * 未採番のジャンルIDを取得する.
     * @return ジャンルID
     */
    public static GenreId notNumberingValue() {

        return new GenreId(null);
    }
}
