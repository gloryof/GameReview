package jp.ne.glory.domain.review.value.search;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.common.value.SearchCondition;
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
    private final List<ReviewId> reviewIds;

    /**
     * ジャンルID.
     */
    @Getter
    private final List<GenreId> genreIds;

    /**
     * コンストラクタ.
     */
    public ReviewSearchCondition() {

        this.reviewIds = new ArrayList<>();
        this.genreIds = new ArrayList<>();
    }

}
