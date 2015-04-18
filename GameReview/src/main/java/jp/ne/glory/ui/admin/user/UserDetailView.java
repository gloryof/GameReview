package jp.ne.glory.ui.admin.user;

import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserName;
import lombok.Getter;

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
     * 権限ラベル（設定変更）
     */
    @Getter
    private final String authorityConfigChangeLabel = Authority.ConfigChange.name;

    /**
     * 権限ラベル（レビュー投稿）
     */
    @Getter
    private final String authorityReviewPostLabel = Authority.ReviewPost.name;

    /**
     * 権限ラベル（権限なし）
     */
    @Getter
    private final String authorityNoneLabel = Authority.None.name;

    /**
     * ユーザID.
     */
    @Getter
    private final Long userId;

    /**
     * ログインID.
     */
    @Getter
    private final String loginId;

    /**
     * ユーザ名.
     */
    @Getter
    private final String userName;

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
    public UserDetailView(final User user) {

        userId = user.getUserId().getValue();
        loginId = user.getLoginId().getValue();
        userName = user.getUserName().getValue();
        configChangeable = user.getAuthorities().hasAuthority(Authority.ConfigChange);
        reviewPostenable = user.getAuthorities().hasAuthority(Authority.ReviewPost);
        noneAuthority = user.getAuthorities().hasAuthority(Authority.None);
    }
}
