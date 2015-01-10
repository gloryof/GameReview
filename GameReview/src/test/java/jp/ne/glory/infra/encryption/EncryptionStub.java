package jp.ne.glory.infra.encryption;

import jp.ne.glory.domain.user.value.Password;

public class EncryptionStub implements Encryption {

    public boolean isEncrypted = false;

    @Override
    public Password encrypt(String value) {

        isEncrypted = true;
        return new Password(value);
    }

}
