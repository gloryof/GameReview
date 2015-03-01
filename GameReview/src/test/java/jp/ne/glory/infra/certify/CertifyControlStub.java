package jp.ne.glory.infra.certify;

import jp.ne.glory.domain.user.value.LoginId;

public class CertifyControlStub implements CertifyControl {

    public boolean isCertify = false;

    @Override
    public CertifySession createAuthentication(final LoginId loginId) {

        isCertify = true;

        return new CertifySession();
    }

}
