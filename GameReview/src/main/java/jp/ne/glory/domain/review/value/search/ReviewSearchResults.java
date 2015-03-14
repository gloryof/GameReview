package jp.ne.glory.domain.review.value.search;

import java.util.List;
import jp.ne.glory.domain.common.value.SearchResults;

/**
 * レビュー検索結果リスト.
 *
 * @author Junki Yamda
 */
public class ReviewSearchResults extends SearchResults<ReviewSearchCondition, ReviewSearchResult> {

    /**
     * コンストラクタ.
     *
     * @param paramCondition 検索条件
     * @param paramResults 検索結果
     * @param paramAllCount 全件数
     */
    public ReviewSearchResults(final ReviewSearchCondition paramCondition,
            final List<ReviewSearchResult> paramResults, int paramAllCount) {

        super(paramCondition, paramResults, paramAllCount);
    }
}
