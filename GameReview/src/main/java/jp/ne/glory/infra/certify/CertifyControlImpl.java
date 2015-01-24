package jp.ne.glory.infra.certify;

import javax.enterprise.context.RequestScoped;
import jp.ne.glory.domain.user.value.LoginId;

/**
 * 認証をコントロールする.<br>
 * 認証はHTTPセッションで行う.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class CertifyControlImpl implements CertifyControl {

    /**
     * 認証情報をセッションに設定する.
     *
     * @param loginId ログインID
     */
    @Override
    public void createAuthentication(final LoginId loginId) {

        System.out.println("certify!");
    }

}
