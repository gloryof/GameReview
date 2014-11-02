package jp.ne.glory.domain.review.value.search;

import java.util.List;

/**
 * レビュー検索結果リスト.
 *
 * @author Junki Yamda
 */
public class ReviewSearchResults {

    /**
     * 検索条件.
     */
    public final ReviewSearchCondition condition;

    /**
     * レビュー検索リスト.
     */
    public final List<ReviewSearchResult> results;

    /**
     * 全件数.
     */
    public final int allCount;

    /**
     * コンストラクタ.
     *
     * @param paramCondition 検索条件
     * @param paramResults 検索結果
     * @param paramAllCount 全件数
     */
    public ReviewSearchResults(final ReviewSearchCondition paramCondition,
            final List<ReviewSearchResult> paramResults, int paramAllCount) {

        condition = paramCondition;
        results = paramResults;
        allCount = paramAllCount;

    }

    /**
     * 次のロットがあるかを判定する.
     *
     * @return ある場合：true、ない場合：false
     */
    public boolean hasNetLot() {

        return false;
    }

    /**
     * 前のロットがあるかを判定する.
     *
     * @return ある場合：true、ない場合：false
     */
    public boolean hasPrevLot() {

        return false;
    }
}
