package jp.ne.glory.domain.review.value.search;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.genre.value.GenreId;

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
    public int targetCount = 0;

    /**
     * 1ロット内の件数.<br>
     * 1未満の場合は全件数が対象となる
     */
    public int lotPerCount = 0;

    /**
     * ロット番号.<br>
     * 取得対象となるロットの番号を指定する。<br>
     * デフォルトは1。
     */
    public int lotNumber = 1;

    /**
     * 検索順序タイプ.<br>
     * デフォルトは投稿日時の降順。
     */
    public ReviewSearchOrderType orderType = ReviewSearchOrderType.PostTimeDesc;

    /**
     * ジャンルID.
     */
    public final List<GenreId> genreIds = new ArrayList<>();
}
