package jp.ne.glory.domain.review.value.search;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.review.value.ReviewId;
import lombok.Getter;
import lombok.Setter;

/**
 * レビュー検索条件.
 *
 * @author Junki Yamada
 */
public class ReviewSearchCondition {

    /**
     * 対象件数.<br>
     * 1未満の場合は全件数が対象となる
     */
    @Getter
    @Setter
    private int targetCount = 0;

    /**
     * 1ロット内の件数.<br>
     * 1未満の場合は全件数が対象となる
     */
    @Getter
    @Setter
    private int lotPerCount = 0;

    /**
     * ロット番号.<br>
     * 取得対象となるロットの番号を指定する。<br>
     * デフォルトは1。
     */
    @Getter
    @Setter
    private int lotNumber = 1;

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
    @Setter
    private final List<ReviewId> reviewIds;

    /**
     * ジャンルID.
     */
    @Getter
    @Setter
    private final List<GenreId> genreIds;

    /**
     * コンストラクタ.
     */
    public ReviewSearchCondition() {

        this.reviewIds = new ArrayList<>();
        this.genreIds = new ArrayList<>();
    }

}
