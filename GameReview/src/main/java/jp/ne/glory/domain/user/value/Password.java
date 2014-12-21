package jp.ne.glory.domain.user.value;

import java.util.Objects;
import jp.ne.glory.domain.common.annotation.Required;
import jp.ne.glory.domain.common.annotation.ValidCharacters;
import jp.ne.glory.domain.common.annotation.param.ValidCharcterType;
import jp.ne.glory.domain.common.type.InputText;

/**
 * パスワード.
 *
 * @author Junki Yamada
 */
@Required(label = Password.LABEL)
@ValidCharacters(value = ValidCharcterType.OnlySingleByteChars, label = Password.LABEL)
public class Password extends InputText {

    /**
     * ラベル.
     */
    public static final String LABEL = "パスワード";

    /**
     * コンストラクタ.
     *
     * @param encryptedValue 暗号化した値
     */
    public Password(final String encryptedValue) {

        super(encryptedValue);
    }

    /**
     * パスワードが一致しているか判定する.
     *
     * @param password パスワード
     * @return 一致している場合：true、一致していない場合：false
     */
    public boolean isMatch(final Password password) {

        if (password == null) {

            return false;
        }

        if (value.isEmpty()) {

            return false;
        }

        return Objects.equals(value, password.value);
    }

    /**
     * 未設定のパスワードを返却する.
     *
     * @return パスワード
     */
    public static Password empty() {

        return new Password("");
    }
}
