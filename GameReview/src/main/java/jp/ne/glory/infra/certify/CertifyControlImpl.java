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

    @Override
    public void createAuthentication(LoginId loginId) {

        System.out.println("certify!");
    }

}
