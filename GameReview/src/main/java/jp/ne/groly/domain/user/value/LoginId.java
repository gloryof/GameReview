/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.groly.domain.user.value;

import jp.ne.groly.domain.common.annotation.MaxLength;
import jp.ne.groly.domain.common.annotation.Required;
import jp.ne.groly.domain.common.annotation.ValidCharacters;
import jp.ne.groly.domain.common.annotation.param.ValidCharcterType;
import jp.ne.groly.domain.common.type.InputText;

/**
 * ログインID.
 * @author Junki Yamada
 */
@Required(label = LoginId.LABEL)
@MaxLength(value = 50, label = LoginId.LABEL)
@ValidCharacters(value = ValidCharcterType.OnlySingleByteChars, label = LoginId.LABEL)
public class LoginId extends InputText {

    /** ラベル. */
    public static final String LABEL = "ログインID";

    /**
     * コンストラクタ.<br>
     * 値を設定する
     * @param paramValue 値 
     */
    public LoginId(final String paramValue) {

        super(paramValue);
    }

    /**
     * 空の値オブジェクトを返却する.
     * @return ログインID
     */
    public static LoginId empty() {

        return new LoginId("");
    }
}
