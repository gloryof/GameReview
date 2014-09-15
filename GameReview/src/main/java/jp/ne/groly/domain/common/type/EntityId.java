package jp.ne.groly.domain.common.type;

import java.util.Optional;
import jp.ne.groly.domain.game.value.GameId;

/**
 * エンティティのID
 * @author Junki Yamada
 */
public class EntityId {
    
    /** 値. */
    public final Long value;

    /** 値が設定されているかのフラグ */
    public final boolean isSetValue;

    /**
     * 値を設定する
     * @param paramValue 値 
     */
    protected EntityId(final Long paramValue) {

        final Optional<Long> optionalValue = Optional.ofNullable(paramValue);
        value = optionalValue.orElse(0L);
        isSetValue = optionalValue.isPresent();
    }

    /**
     * IDが同じか比較する.
     * 
     * @param paramValue 比較対象ID
     * @return 一致している場合：true、一致していない場合：false
     */
    public boolean isSame(final EntityId paramValue) {

        final Optional<EntityId> optionalValue = Optional.ofNullable(paramValue);
        if (!optionalValue.isPresent()) {

            return false;
        }

        if (isSetValue != paramValue.isSetValue) {

            return false;
        }

        return this.value.equals(optionalValue.get().value);
    }
}
