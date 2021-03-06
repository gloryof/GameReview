package jp.ne.glory.application.review;

import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.review.value.ReviewId;
import lombok.Getter;

/**
 * レビュー投稿結果.
 *
 * @author Junki Yamada
 */
public class ReviewPostResult {

    /**
     * 入力チェック結果.
     */
    @Getter
    private final ValidateErrors errors;

    /**
     * 投稿されたレビューID.
     */
    @Getter
    private final ReviewId postedReviewId;

    /**
     * コンストラクタ.
     *
     * @param paramErrors 入力チェック結果
     * @param paramReviewId レビューID
     */
    public ReviewPostResult(final ValidateErrors paramErrors, final ReviewId paramReviewId) {

        this.errors = paramErrors;
        this.postedReviewId = paramReviewId;
    }
}
