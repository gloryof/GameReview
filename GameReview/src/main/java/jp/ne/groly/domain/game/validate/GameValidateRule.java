/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.groly.domain.game.validate;

import java.util.Optional;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.game.entity.Game;
import jp.ne.groly.domain.game.value.CeroRating;
import jp.ne.groly.domain.genre.entity.Genre;
import jp.ne.groly.domain.genre.value.GenreId;

/**
 * ゲームの入力に関するルール.
 * @author Junki Yamada
 */
public class GameValidateRule {

    /** チェック対象ゲーム. */
    private final Game game;

    /**
     * コンストラクタ.
     * @param paramGame ゲーム
     */
    public GameValidateRule(final Game paramGame) {

        this.game = paramGame;
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

        ValidateErrors errors = new ValidateErrors();

        if (!game.isRegistered()) {

            errors.add(new ValidateError(ErrorInfo.NotRegister, Game.LABEL));
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

        errors.addAll(game.title.validate());
        errors.addAll(game.url.validate());

        final CeroRating checkCero = Optional.ofNullable(game.ceroRating).orElse(CeroRating.Empty);
        if (CeroRating.Empty.equals(checkCero)) {

            errors.add(new ValidateError(ErrorInfo.Required, CeroRating.LABEL));
        }

        final GenreId checkGenre = Optional.ofNullable(game.genreId).orElse(GenreId.notNumberingValue());
        if (!checkGenre.isSetValue) {
            
            errors.add(new ValidateError(ErrorInfo.Required, Genre.LABEL));
        }

        return errors;
    }
}
