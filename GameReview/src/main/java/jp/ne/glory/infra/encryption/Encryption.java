/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ne.glory.infra.encryption;

import jp.ne.glory.domain.user.value.Password;

/**
 * 暗号化クラス.
 *
 * @author Junki Yamada
 */
public interface Encryption {

    /**
     * 暗号化処理.
     *
     * @param value 値
     * @return パスワード
     */
    Password encrypt(final String value);
}
