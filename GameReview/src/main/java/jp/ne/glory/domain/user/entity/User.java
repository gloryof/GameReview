package jp.ne.glory.domain.user.entity;

import jp.ne.glory.domain.user.value.Authorities;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;

/**
 * ユーザ.
 * @author Junki Yamada
 */
public class User {

    /** ラベル. */
    public static final String LABEL = "ユーザ";

    /** ユーザID. */
    public final UserId userId;

    /** 権限. */
    public final Authorities authorities;

    /** ログインID. */
    public LoginId loginId = LoginId.empty();

    /** ユーザ名. */
    public UserName userName = UserName.empty();

    /**
     * パスワード.
     */
    public Password password = Password.empty();

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
}
