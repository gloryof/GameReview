package jp.ne.groly.domain.user.entity;

import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.common.type.Validatable;
import jp.ne.groly.domain.user.value.Authorities;
import jp.ne.groly.domain.user.value.LoginId;
import jp.ne.groly.domain.user.value.UserId;
import jp.ne.groly.domain.user.value.UserName;

/**
 * ユーザ.
 * @author Junki Yamada
 */
public class User implements Validatable {

    /** ユーザID. */
    public final UserId userId;

    /** 権限. */
    public final Authorities authorities;

    /** ログインID. */
    public LoginId loginId = LoginId.empty();

    /** ユーザ名. */
    public UserName userName = UserName.empty();

    /**
     * コンストラクタ.
     * @param paramUserId ユーザID
     */
    public User(final UserId paramUserId) {

        userId = paramUserId;
        authorities = new Authorities();
    }

    /**
     * コンストラクタ
     */
    public User() {

        this(UserId.notNumberingValue());
    }

    /**
     * 登録済みのユーザかを判定する.
     * @return 登録済みの場合：true、未登録の場合：false
     */
    public boolean isRegistered() {
        
        return userId.isSetValue;
    }

    /**
     * 入力情報の検証を行う.
     * @return 検証結果
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors = new ValidateErrors();

        errors.addAll(loginId.validate());
        errors.addAll(userName.validate());
        errors.addAll(authorities.validate());

        return errors;
    }

}
