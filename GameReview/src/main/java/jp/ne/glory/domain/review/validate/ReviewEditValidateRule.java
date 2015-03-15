package jp.ne.glory.domain.review.validate;

import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.validate.ValidateRule;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.review.entity.Review;

/**
 * レビューの編集に関するルール.
 *
 * @author Junki Yamada
 */
public class ReviewEditValidateRule implements ValidateRule {

    /**
     * 共通ルール.
     */
    private final ReviewModifyCommonValidateRule commonRule;

    /**
     * チェック対象レビュー.
     */
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
    public ReviewEditValidateRule(final Review paramReview, final Game paramGame) {

        commonRule = new ReviewModifyCommonValidateRule(paramReview, paramGame);
        review = paramReview;
        game = paramGame;
    }

    /**
     * 編集時の入力チェックを行う.
     * @return エラー情報
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors = new ValidateErrors();

        if (!review.isRegistered()) {

            errors.add(new ValidateError(ErrorInfo.NotRegister, Review.LABEL));
        }

        errors.addAll(commonRule.validate());

        return errors;
    }
}
