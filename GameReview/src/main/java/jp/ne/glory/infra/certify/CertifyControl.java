package jp.ne.glory.infra.certify;

import jp.ne.glory.domain.user.value.LoginId;

/**
 * 認証をコントロールする.
 *
 * @author Junki Yamada
 */
public interface CertifyControl {

    /**
     * 認証情報を作成する.
     *
     * @param loginId ログインID
     */
    void createAuthentication(final LoginId loginId);
}
