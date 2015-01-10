package jp.ne.glory.ui.login;

import javax.ws.rs.FormParam;
import jp.ne.glory.domain.common.error.ValidateErrors;

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
    public String loginId;

    /**
     * パスワード.
     */
    @FormParam("password")
    public String password;

    /**
     * 入力チェック結果.
     */
    public final ValidateErrors errors = new ValidateErrors();
}
