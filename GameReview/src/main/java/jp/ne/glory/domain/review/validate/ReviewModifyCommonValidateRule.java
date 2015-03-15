package jp.ne.glory.domain.review.validate;

import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.validate.ValidateRule;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.review.entity.Review;

/**
 * レビューの編集に関する共通ルール.
 *
 * @author Junki Yamada
 */
public class ReviewModifyCommonValidateRule implements ValidateRule {

    /** チェック対象レビュー. */
    private final Review review;

    /**
     * レビューに紐付くゲーム.
     */
    private final Game game;

    /**
     * コンストラクタ.
     * @param paramReview レビュー
     * @param paramGame ゲーム
     */
    public ReviewModifyCommonValidateRule(final Review paramReview, final Game paramGame) {

        review = paramReview;
        game = paramGame;
    }

    /**
     * 入力情報の検証を行う.
     * @return 検証結果
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors = new ValidateErrors();

        errors.addAll(review.getGoodPoint().validate());
        errors.addAll(review.getBadPoint().validate());
        errors.addAll(review.getComment().validate());
        errors.addAll(review.getScore().validate());

        final ValidateError gameValidateError = validateRelationGame();
        if (gameValidateError != null) {

            errors.add(gameValidateError);
        }

        return errors;
    }

    /**
     * 紐付くゲーム情報の検証を行う.
     *
     * @return 検証結果
     */
    private ValidateError validateRelationGame() {


        if (game == null) {

            return new ValidateError(ErrorInfo.NotInputInfo, Game.LABEL);
        }

        if (review.getGameId() == null) {

            return new ValidateError(ErrorInfo.NotSettingRelation, Game.LABEL);
        }

        if (!review.getGameId().isSame(game.getId())) {

            return new ValidateError(ErrorInfo.MismatchRelation, Game.LABEL);
        }

        return null;
    }
}
