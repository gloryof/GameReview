package jp.ne.glory.domain.genre.validate;

import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.validate.ValidateRule;
import jp.ne.glory.domain.genre.entity.Genre;

/**
 * ジャンルの編集に関するルール.
 *
 * @author Junki Yamada
 */
public class GenreEditValidateRule implements ValidateRule {

    /**
     * 共通ルール.
     */
    private final GenreModfyCommonValidateRule commonRule;

    /**
     * チェック対象ジャンル.
     */
    private final Genre genre;

    /**
     * コンストラクタ.
     * 
     * @param genre ジャンル
     */
    public GenreEditValidateRule(final Genre genre) {

        commonRule = new GenreModfyCommonValidateRule(genre);
        this.genre = genre;
    }


    /**
     * 編集時の入力チェックを行う.
     * @return エラー情報
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors = new ValidateErrors();

        if (!genre.isRegistered()) {

            errors.add(new ValidateError(ErrorInfo.NotRegister, Genre.LABEL));
        }

        errors.addAll(commonRule.validate());
 
        return errors;
    }

}
