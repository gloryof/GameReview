/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ne.glory.domain.genre.validate;

import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.validate.ValidateRule;
import jp.ne.glory.domain.genre.entity.Genre;

/**
 * ジャンルの編集に関する共通ルール.
 *
 * @author Junki Yamada
 */
public class GenreModfyCommonValidateRule implements ValidateRule {

    /** チェック対象ジャンル. */
    private final Genre genre;

    /**
     * コンストラクタ.
     * 
     * @param paramGenre ジャンル 
     */
    public GenreModfyCommonValidateRule(final Genre paramGenre) {

        genre = paramGenre;
    }

    /**
     * 入力情報の検証を行う.
     * @return 検証結果
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors = new ValidateErrors();

        errors.addAll(genre.getName().validate());

        return errors;
    }
}
