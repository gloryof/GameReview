package jp.ne.glory.domain.game.validate;

import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.validate.ValidateRule;
import jp.ne.glory.domain.game.entity.Game;

/**
 * ゲームの編集に関するルール.
 *
 * @author Junki Yamada
 */
public class GameEditValidateRule implements ValidateRule {

    /**
     * 共通ルール.
     */
    private final GameModifyCommonValidateRule commonRule;

    /**
     * チェック対象ゲーム.
     */
    private final Game game;

    /**
     * コンストラクタ.
     * @param paramGame ゲーム
     */
    public GameEditValidateRule(final Game paramGame) {

        commonRule = new GameModifyCommonValidateRule(paramGame);
        this.game = paramGame;
    }

    /**
     * 編集時の入力チェックを行う.
     * @return エラー情報
     */
    @Override
    public ValidateErrors validate() {

        ValidateErrors errors = new ValidateErrors();

        if (!game.isRegistered()) {

            errors.add(new ValidateError(ErrorInfo.NotRegister, Game.LABEL));
        }

        errors.addAll(commonRule.validate());

        return errors;
    }
}
