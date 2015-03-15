package jp.ne.glory.domain.game.validate;

import java.util.Optional;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.validate.ValidateRule;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;

/**
 * ゲームの入力に関するルール.
 * @author Junki Yamada
 */
public class GameModifyCommonValidateRule implements ValidateRule {

    /** チェック対象ゲーム. */
    private final Game game;

    /**
     * コンストラクタ.
     * @param paramGame ゲーム
     */
    public GameModifyCommonValidateRule(final Game paramGame) {

        this.game = paramGame;
    }

    /**
     * 入力情報の検証を行う.
     * @return 検証結果
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors = new ValidateErrors();

        errors.addAll(game.getTitle().validate());
        errors.addAll(game.getUrl().validate());

        final CeroRating checkCero = Optional.ofNullable(game.getCeroRating()).orElse(CeroRating.Empty);
        if (CeroRating.Empty.equals(checkCero)) {

            errors.add(new ValidateError(ErrorInfo.Required, CeroRating.LABEL));
        }

        final GenreId checkGenre = Optional.ofNullable(game.getGenreId()).orElse(GenreId.notNumberingValue());
        if (!checkGenre.isSetValue()) {
            
            errors.add(new ValidateError(ErrorInfo.Required, Genre.LABEL));
        }

        return errors;
    }
}
