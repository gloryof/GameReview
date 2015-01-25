package jp.ne.glory.application.user;

import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.value.UserId;
import lombok.Getter;

/**
 * ユーザ情報登録の結果.
 *
 * @author Junki Yamada
 */
public class UserRegisterResult {

    /**
     * 入力チェック結果.
     */
    @Getter
    private final ValidateErrors errors;

    /**
     * 登録されたユーザID.
     */
    @Getter
    private final UserId registeredUserId;

    /**
     * コンストラクタ.
     *
     * @param errors 入力チェック結果
     * @param registeredUserId 登録されたユーザID
     */
    public UserRegisterResult(final ValidateErrors errors, final UserId registeredUserId) {

        this.errors = errors;
        this.registeredUserId = registeredUserId;
    }
}
