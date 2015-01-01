package jp.ne.glory.application.genre;

import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.genre.value.GenreId;

/**
 * ジャンル登録結果.
 *
 * @author Junki Yamada
 */
public class GenreRegisterResult {

    /**
     * 入力チェック結果.
     */
    public final ValidateErrors errors;

    /**
     * 登録されたジャンルID.
     */
    public final GenreId registeredGenreId;

    /**
     * コンストラクタ.
     *
     * @param paramErrors 入力チェック結果
     * @param paramGenreId 登録されたジャンルID
     */
    public GenreRegisterResult(final ValidateErrors paramErrors, final GenreId paramGenreId) {

        errors = paramErrors;
        registeredGenreId = paramGenreId;
    }
}
