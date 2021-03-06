package jp.ne.glory.ui.admin.review;

import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import lombok.Getter;
import lombok.Setter;

/**
 * レビュー編集View.
 *
 * @author Junki Yamada
 */
public class ReviewEditView {

    /**
     * ラベル：タイトル.
     */
    @Getter
    private final String tileLabel = Title.LABEL;

    /**
     * ラベル：URL.
     */
    @Getter
    private final String urlLabel = SiteUrl.LABEL;

    /**
     * ラベル：CEROレーティング.
     */
    @Getter
    private final String ceroLabel = CeroRating.LABEL;

    /**
     * ラベル：ジャンル.
     */
    @Getter
    private final String genreLabel = Genre.LABEL;

    /**
     * ラベル：良い点.
     */
    @Getter
    private final String goodPointLabel = GoodPoint.LABEL;

    /**
     * ラベル：悪い点.
     */
    @Getter
    private final String badPointLabel = BadPoint.LABEL;

    /**
     * ラベル：コメント.
     */
    @Getter
    private final String commentLabel = Comment.LABEL;

    /**
     * ラベル：スコア.
     */
    @Getter
    private final String scoreLabel = Score.LABEL;

    /**
     * レビュー情報.
     */
    @Getter
    @Setter
    private ReviewBean review;

    /**
     * 入力チェック結果.
     */
    @Getter
    private final ValidateErrors errors = new ValidateErrors();

    /**
     * コンストラクタ.
     */
    public ReviewEditView() {

        super();
    }

    /**
     * コンストラクタ.
     *
     * @param result レビュー検索結果.
     */
    public ReviewEditView(final ReviewSearchResult result) {

        this.review = new ReviewBean(result);
    }
}
