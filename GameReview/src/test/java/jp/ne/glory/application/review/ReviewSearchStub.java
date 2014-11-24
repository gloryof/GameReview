package jp.ne.glory.application.review;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchOrderType;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;

public class ReviewSearchStub extends ReviewSearch {
    
    private final List<ReviewSearchResult> testData = new ArrayList<>();

    public ReviewSearchStub(final List<ReviewSearchResult> initData) {

        super();
        testData.addAll(initData);
    }

    @Override
    public ReviewSearchResults searchNewReviews(final int count) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();
        condition.targetCount = count;
        condition.lotPerCount = count;
        condition.orderType = ReviewSearchOrderType.PostTimeDesc;

        return search(condition);
    }

    private ReviewSearchResults search(final ReviewSearchCondition condition) {

        final List<ReviewSearchResult> searchResults = testData
                .stream()
                .filter(v -> true)
                .limit(condition.lotPerCount)
                .collect(Collectors.toList());
        final int resultCount = searchResults.size();

        return new ReviewSearchResults(condition, searchResults, resultCount);
    }
}
