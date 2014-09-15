package jp.ne.glory.domain.game.value;

import jp.ne.glory.domain.common.type.EntityId;

/**
 * ゲームID.
 * ゲームを一意に決めるためのID
 * @author Junki Yamada
 */
public class GameId extends EntityId {

    /** ラベル */
    public static final String LABEL = "ゲームID";

    /**
     * コンストラクタ.<br>
     * 値を設定する
     * @param paramValue 値 
     */
    public GameId(final Long paramValue) {

        super(paramValue);
    }

    /**
     * 未採番のゲームIDを取得する.
     * @return ゲームID
     */
    public static GameId notNumberingValue() {

        return new GameId(null);
    }
}
