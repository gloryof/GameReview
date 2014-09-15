package jp.ne.groly.domain.review.validate;

import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.review.entity.Review;

/**
 * レビューの入力に関するルール.
 * @author Junki Yamada
 */
public class ReviewValidateRule {

    /** チェック対象レビュー. */
    public final Review review;

    /**
     * コンストラクタ.
     * @param paramReview レビュー 
     */
    public ReviewValidateRule(final Review paramReview) {

        review = paramReview;
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

        if (!review.isRegistered()) {

            errors.add(new ValidateError(ErrorInfo.NotRegister, Review.LABEL));
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

        errors.addAll(review.gooodPoint.validate());
        errors.addAll(review.badPoint.validate());
        errors.addAll(review.comment.validate());
        errors.addAll(review.score.validate());

        return errors;
    }
}
