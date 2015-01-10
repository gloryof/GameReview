package jp.ne.glory.application.user;

import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;

public class UserAuthenticationStub extends UserAuthentication {

    public boolean certifyComplete = false;

    public UserAuthenticationStub() {
        super(null);
    }

    @Override
    public boolean isCertify(final LoginId loginId, final Password password) {

        return certifyComplete;
    }
}
