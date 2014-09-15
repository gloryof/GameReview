/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.groly.domain.genre.value;

import jp.ne.groly.domain.common.annotation.MaxLength;
import jp.ne.groly.domain.common.annotation.Required;
import jp.ne.groly.domain.common.type.InputText;

/**
 * ジャンル名.
 * @author Junki Yamada
 */
@Required(label = GenreName.LABEL)
@MaxLength(value = 50, label = GenreName.LABEL)
public class GenreName extends InputText {

    /** ラベル. */
    public static final String LABEL = "ジャンル";

    /**
     * コンストラクタ.<br>
     * 値を設定する
     * @param paramValue 値 
     */
    public GenreName(final String paramValue) {

        super(paramValue);
    }

    /**
     * 空の値オブジェクトを返却する.
     * @return ジャンル名
     */
    public static GenreName empty() {

        return new GenreName("");
    }
}
