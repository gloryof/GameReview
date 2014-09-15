/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.groly.domain.game.value;

import jp.ne.groly.domain.common.annotation.MaxLength;
import jp.ne.groly.domain.common.annotation.Required;
import jp.ne.groly.domain.common.type.InputText;

/**
 * タイトル.
 * @author Junki Yamada
 */
@Required(label = Title.LABEL)
@MaxLength(value = 100, label = Title.LABEL)
public class Title extends InputText {

    /** ラベル. */
    public static final String LABEL = "タイトル";

    /**
     * コンストラクタ.<br>
     * 値を設定する
     * @param paramValue 値 
     */
    public Title(final String paramValue) {

        super(paramValue);
    }

    /**
     * 空の値オブジェクトを返却する.
     * @return タイトル
     */
    public static Title empty() {

        return new Title("");
    }
}
