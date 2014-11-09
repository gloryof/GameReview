package jp.ne.glory.application.review;

import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import jp.ne.glory.domain.review.repository.ReviewRepository;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchOrderType;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;

/**
 * レビュー検索.
 *
 * @author Junki Yamada.
 */
@Dependent
public class ReviewSearch {

    /**
     * レビューリポジトリ.
     */
    private final ReviewRepository repository;

    /**
     * コンストラクタ.
     *
     * @param repository リポジトリ
     */
    @Inject
    public ReviewSearch(final ReviewRepository repository) {

        this.repository = repository;
    }

    /**
     * 最新のレビューを取得する.
     *
     * @param count 取得件数
     * @return レビュー検索結果
     */
    public ReviewSearchResults searchNewReviews(final int count) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();
        condition.targetCount = count;
        condition.orderType = ReviewSearchOrderType.PostTimeDesc;

        final List<ReviewSearchResult> resultList = repository.search(condition);
        final int resultCount = repository.getSearchCount(condition);

        return new ReviewSearchResults(condition, resultList, resultCount);
    }
}
