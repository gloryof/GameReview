package jp.ne.glory.ui.admin.user;

import jp.ne.glory.domain.user.entity.User;
import lombok.Getter;

/**
 * ユーザ情報Bean.
 *
 * @author Junki Yamada
 */
public class UserBean {

    /**
     * コンストラクタ.
     *
     * @param user ユーザ
     */
    public UserBean(final User user) {

        this.userId = user.getUserId().getValue();
        this.userName = user.getUserName().getValue();
        this.loginId = user.getLoginId().getValue();

    }

    /**
     * ユーザID.
     */
    @Getter
    private final Long userId;

    /**
     * ユーザ名.
     */
    @Getter
    private final String userName;

    /**
     * ログインID.
     */
    @Getter
    private final String loginId;

}
