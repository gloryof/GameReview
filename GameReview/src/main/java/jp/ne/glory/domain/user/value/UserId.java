/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.glory.domain.user.value;

import jp.ne.glory.domain.common.type.EntityId;

/**
 * ユーザID.<br>
 * ユーザをユニークに識別するためのID。<br>
 * ログインするためのログインIDとは別。
 * @author Admin
 */
public class UserId extends EntityId {

    /**
     * 値を設定する
     * @param paramValue 値 
     */
    public UserId(final Long paramValue) {

        super(paramValue);
    }
    /**
     * 未採番のレビューIDを取得する.
     * @return レビューID
     */
    public static UserId notNumberingValue() {

        return new UserId(null);
    }
}
