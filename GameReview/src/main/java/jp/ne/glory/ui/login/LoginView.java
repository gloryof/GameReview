package jp.ne.glory.ui.login;

import javax.ws.rs.FormParam;
import jp.ne.glory.domain.common.error.ValidateErrors;
import lombok.Getter;
import lombok.Setter;

/**
 * ログインビュー.
 *
 * @author Junki Yamada
 */
public class LoginView {

    /**
     * ログインID.
     */
    @FormParam("loginId")
    @Getter
    @Setter
    private String loginId;

    /**
     * パスワード.
     */
    @FormParam("password")
    @Getter
    @Setter
    private String password;

    /**
     * 入力チェック結果.
     */
    @Getter
    private final ValidateErrors errors = new ValidateErrors();
}
