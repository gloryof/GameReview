package jp.ne.glory.ui.admin.user;

import java.util.List;
import javax.ws.rs.FormParam;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserName;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザ編集View.
 *
 * @author Junki Yamada
 */
public class UserEditView {

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
    private final Authority authorityConfigChange = Authority.ConfigChange;

    /**
     * 権限ラベル（レビュー投稿）
     */
    @Getter
    private final Authority authorityReviewPost = Authority.ReviewPost;

    /**
     * ユーザID.
     */
    @Getter
    @Setter
    @FormParam("userId")
    private Long userId;

    /**
     * ログインID.
     */
    @Getter
    @Setter
    @FormParam("loginId")
    private String loginId;

    /**
     * ユーザ名.
     */
    @Getter
    @Setter
    @FormParam("userName")
    private String userName;

    /**
     * パスワード.
     */
    @Getter
    @Setter
    @FormParam("password")
    private String password;

    /**
     * 権限変更可能フラグ.
     */
    @Getter
    @Setter
    private boolean configChangeable;

    /**
     * レビュー投稿可能フラグ.
     */
    @Getter
    @Setter
    private boolean reviewPostenable;

    /**
     * 権限チェックボックス.
     */
    @Getter
    @Setter
    @FormParam("authorities")
    private List<String> authorityValues;

    /**
     * 入力チェック結果.
     */
    @Getter
    private final ValidateErrors errors = new ValidateErrors();

    /**
     * コンストラクタ.
     */
    public UserEditView() {
        super();
    }

    /**
     * コンストラクタ.
     *
     * @param user ユーザ
     */
    public UserEditView(final User user) {

        userId = user.getUserId().getValue();
        loginId = user.getLoginId().getValue();
        userName = user.getUserName().getValue();
        configChangeable = user.getAuthorities().hasAuthority(Authority.ConfigChange);
        reviewPostenable = user.getAuthorities().hasAuthority(Authority.ReviewPost);
        authorityValues = null;
    }
}
