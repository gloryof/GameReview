package jp.ne.glory.ui.admin.user;

import jp.ne.glory.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザ詳細View.
 *
 * @author Junki Yamada
 */
public class UserDetailView {

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

    public UserDetailView(final User user) {

        userId = user.getUserId().getValue();
        loginId = user.getLoginId().getValue();
        userName = user.getUserName().getValue();
    }
}
