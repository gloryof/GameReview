/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ne.glory.domain.genre.validate;

import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.genre.entity.Genre;

/**
 * ジャンルの入力に関するルール.
 * @author Junki Yamada
 */
public class GenreValidateRule {

    /** チェック対象ジャンル. */
    private final Genre genre;

    /**
     * コンストラクタ.
     * 
     * @param paramGenre ジャンル 
     */
    public GenreValidateRule(final Genre paramGenre) {

        genre = paramGenre;
    }

    /**
     * 登録時の入力チェックを行う.
     * @return エラー情報
     */
    public ValidateErrors validateForRegister() {

        return validateCommon();
    }

    /**
     * 編集時の入力チェックを行う.
     * @return エラー情報
     */
    public ValidateErrors validateForEdit() {

        final ValidateErrors errors = new ValidateErrors();

        if (!genre.isRegistered()) {

            errors.add(new ValidateError(ErrorInfo.NotRegister, Genre.LABEL));
        }

        errors.addAll(validateCommon());
 
        return errors;
    }

    /**
     * 入力情報の検証を行う.
     * @return 検証結果
     */
    private ValidateErrors validateCommon() {

        final ValidateErrors errors = new ValidateErrors();

        errors.addAll(genre.getName().validate());

        return errors;
    }
}
