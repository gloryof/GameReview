package jp.ne.glory.domain.review.value.search;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.common.type.DateValue;
import jp.ne.glory.domain.common.value.SearchCondition;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.review.value.ReviewId;
import lombok.Getter;
import lombok.Setter;

/**
 * レビュー検索条件.
 *
 * @author Junki Yamada
 */
public class ReviewSearchCondition extends SearchCondition {

    /**
     * 検索順序タイプ.<br>
     * デフォルトは投稿日時の降順。
     */
    @Getter
    @Setter
    private ReviewSearchOrderType orderType = ReviewSearchOrderType.PostTimeDesc;

    /**
     * レビューID.
     */
    @Getter
    private final List<ReviewId> reviewIds = new ArrayList<>();

    /**
     * ジャンルID.
     */
    @Getter
    private final List<GenreId> genreIds = new ArrayList<>();

    /**
     * CEROレーティング.
     */
    @Getter
    private final List<CeroRating> ceroRatigns = new ArrayList<>();

    /**
     * タイトル
     */
    @Setter
    @Getter
    private String title = null;

    /**
     * 投稿日時：From
     */
    @Setter
    @Getter
    private DateValue postedFrom = null;

    /**
     * 投稿日時：To
     */
    @Setter
    @Getter
    private DateValue postedTo = null;

    /**
     * 対象となるレビューIDを追加する.
     *
     * @param id レビューID
     */
    public void addReviewId(final ReviewId id) {

        reviewIds.add(id);
    }

    /**
     * 対象となるジャンルIDを追加する.
     *
     * @param genreId ジャンルID
     */
    public void addGenreId(final GenreId genreId) {

        genreIds.add(genreId);
    }

    /**
     * 対象となるCEROレーティングを追加する.
     *
     * @param rating
     */
    public void addCeroRating(final CeroRating rating) {

        ceroRatigns.add(rating);
    }
}
