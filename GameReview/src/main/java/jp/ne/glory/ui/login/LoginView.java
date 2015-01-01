package jp.ne.glory.ui.login;

import javax.ws.rs.FormParam;

/**
 * ログインビュー.
 *
 * @author Junki Yamada
 */
public class LoginView {

    /**
     * ユーザID.
     */
    @FormParam("userId")
    public String userId;

    /**
     * パスワード.
     */
    @FormParam("password")
    public String password;
}
