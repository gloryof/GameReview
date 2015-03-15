package jp.ne.glory.domain.user.validate;

import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.validate.ValidateRule;
import jp.ne.glory.domain.user.entity.User;

/**
 * ユーザの編集に関する共通ルール.
 *
 * @author Junki Yamada
 */
public class UserModifyCommonValidateRule implements ValidateRule {

    /** チェック対象ユーザ. */
    private final User user;

    /**
     * コンストラクタ.
     * @param paramUser ユーザ. 
     */
    public UserModifyCommonValidateRule(final User paramUser) {

        user = paramUser;
    }

    /**
     * 入力情報の検証を行う.
     * @return 検証結果
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors = new ValidateErrors();

        errors.addAll(user.getLoginId().validate());
        errors.addAll(user.getUserName().validate());
        errors.addAll(user.getAuthorities().validate());
        errors.addAll(user.getPassword().validate());

        return errors;
    }
}
