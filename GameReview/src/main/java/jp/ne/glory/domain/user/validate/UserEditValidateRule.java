package jp.ne.glory.domain.user.validate;

import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.validate.ValidateRule;
import jp.ne.glory.domain.user.entity.User;

/**
 * ユーザ情報の変更に関する入力ルール.
 *
 * @author Junki Yamada
 */
public class UserEditValidateRule implements ValidateRule {

    /**
     * 共通ルール.
     */
    private final UserModifyCommonValidateRule commonRule;

    /** チェック対象ユーザ. */
    private final User user;

    /**
     * コンストラクタ.
     * @param paramUser ユーザ. 
     */
    public UserEditValidateRule(final User paramUser) {

        user = paramUser;
        commonRule = new UserModifyCommonValidateRule(paramUser);
    }

    /**
     * 編集時の入力チェックを行う.
     * @return エラー情報
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors = commonRule.validate();

        if (!user.isRegistered()) {

            errors.add(new ValidateError(ErrorInfo.NotRegister, User.LABEL));
        }

        return errors;
    }

}
