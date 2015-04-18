package jp.ne.glory.ui.admin.user;

import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.Authority;
import lombok.Getter;

/**
 * ユーザ情報Bean.
 *
 * @author Junki Yamada
 */
public class UserBean {

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

    /**
     * 権限変更可能フラグ.
     */
    @Getter
    private final boolean configChangeable;

    /**
     * レビュー投稿可能フラグ.
     */
    @Getter
    private final boolean reviewPostenable;

    /**
     * 権限なしフラグ.
     */
    @Getter
    private final boolean noneAuthority;

    /**
     * コンストラクタ.
     *
     * @param user ユーザ
     */
    public UserBean(final User user) {

        this.userId = user.getUserId().getValue();
        this.userName = user.getUserName().getValue();
        this.loginId = user.getLoginId().getValue();
        configChangeable = user.getAuthorities().hasAuthority(Authority.ConfigChange);
        reviewPostenable = user.getAuthorities().hasAuthority(Authority.ReviewPost);
        noneAuthority = user.getAuthorities().hasAuthority(Authority.None);
    }
}
