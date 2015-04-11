package jp.ne.glory.ui.admin.user;

import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserName;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザ詳細View.
 *
 * @author Junki Yamada
 */
public class UserDetailView {

    /**
     * ログインラベル.
     */
    @Getter
    private final String loginIdLabel = LoginId.LABEL;

    /**
     * ユーザ名ラベル.
     */
    @Getter
    private final String userNameLabel = UserName.LABEL;

    /**
     * パスワードラベル.
     */
    @Getter
    private final String passwordLabel = Password.LABEL;

    /**
     * ユーザID.
     */
    @Getter
    @Setter
    private Long userId;

    /**
     * ログインID.
     */
    @Getter
    @Setter
    private String loginId;

    /**
     * ユーザ名.
     */
    @Getter
    @Setter
    private String userName;

    /**
     * パスワード.
     */
    @Getter
    @Setter
    private String password;

    /**
     * コンストラクタ.
     *
     * @param user ユーザ
     */
    public UserDetailView(final User user) {

        userId = user.getUserId().getValue();
        loginId = user.getLoginId().getValue();
        userName = user.getUserName().getValue();
    }
}
