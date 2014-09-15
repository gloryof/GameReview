package jp.ne.groly.domain.genre.value;

import jp.ne.groly.domain.common.type.EntityId;

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
