package jp.ne.glory.infra.encryption;

import jp.ne.glory.domain.user.value.Password;

public class EncryptionStub implements Encryption {

    @Override
    public Password encrypt(String value) {

        return new Password(value);
    }

}
