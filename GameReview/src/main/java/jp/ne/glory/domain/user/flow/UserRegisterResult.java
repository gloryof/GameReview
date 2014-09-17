package jp.ne.glory.domain.user.flow;

import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.value.UserId;

/**
 * ユーザ情報登録の結果.
 *
 * @author Junki Yamada
 */
public class UserRegisterResult {

    /**
     * 入力チェック結果.
     */
    public final ValidateErrors erros;

    /**
     * 登録されたユーザID.
     */
    public final UserId registeredUserId;

    /**
     * コンストラクタ.
     *
     * @param errors 入力チェック結果
     * @param registeredUserId 登録されたユーザID
     */
    public UserRegisterResult(final ValidateErrors errors, final UserId registeredUserId) {

        this.erros = errors;
        this.registeredUserId = registeredUserId;
    }
}
