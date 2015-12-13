package jp.ne.glory.infra.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        try {

            final MessageDigest digest = MessageDigest.getInstance("SHA-256");

            final byte[] encryptedValues = digest.digest(value.getBytes());
            final StringBuilder builder = new StringBuilder();
            for (final byte encryptedValue : encryptedValues) {

                builder.append(String.format("%02x", encryptedValue));
            }

            return new Password(builder.toString());
        } catch (NoSuchAlgorithmException ex) {

            throw new RuntimeException(ex);
        }
    }

}
