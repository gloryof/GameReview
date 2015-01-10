package jp.ne.glory.infra.encryption;

import javax.enterprise.context.RequestScoped;
import jp.ne.glory.domain.user.value.Password;

/**
 * 暗号化クラス.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class EncryptionImpl implements Encryption {

    @Override
    public Password encrypt(String value) {

        return new Password(value);
    }

}
