package jp.ne.glory.domain.user.validate;

import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.entity.User;

/**
 * ユーザ情報に関する入力ルール.
 * @author Junki Yamada
 */
public class UserValidateRule {

    /** チェック対象ユーザ. */
    private final User user;

    /**
     * コンストラクタ.
     * @param paramUser ユーザ. 
     */
    public UserValidateRule(final User paramUser) {

        user = paramUser;
    }

    /**
     * 登録時の入力チェックを行う.
     * @return エラー情報
     */
    public ValidateErrors validateForRegister() {
        return validateCommon();
    }

    /**
     * 編集時の入力チェックを行う.
     * @return エラー情報
     */
    public ValidateErrors validateForEdit() {

        final ValidateErrors errors = validateCommon();

        if (!user.isRegistered()) {

            errors.add(new ValidateError(ErrorInfo.NotRegister, User.LABEL));
        }

        return errors;
    }

    /**
     * 入力情報の検証を行う.
     * @return 検証結果
     */
    private ValidateErrors validateCommon() {

        final ValidateErrors errors = new ValidateErrors();

        errors.addAll(user.loginId.validate());
        errors.addAll(user.userName.validate());
        errors.addAll(user.authorities.validate());
        errors.addAll(user.password.validate());

        return errors;
    }
}
