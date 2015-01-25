package jp.ne.glory.application.game;

import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.game.value.GameId;
import lombok.Getter;

/**
 * ゲーム登録結果.
 *
 * @author Junki Yamada
 */
public class GameRegisterResult {

    /**
     * 入力チェック結果.
     */
    @Getter
    private final ValidateErrors errors;

    /**
     * 登録されたゲームID.
     */
    @Getter
    private final GameId registeredGameId;

    /**
     * コンストラクタ.
     *
     * @param paramErrors 入力チェック結果
     * @param paramGameId 登録されたゲームID
     */
    public GameRegisterResult(final ValidateErrors paramErrors, final GameId paramGameId) {

        errors = paramErrors;
        registeredGameId = paramGameId;
    }
}
