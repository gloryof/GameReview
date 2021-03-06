package jp.ne.glory.application.review;

import java.util.List;
import java.util.Optional;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.review.repository.ReviewRepositoryStub;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchOrderType;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;

public class ReviewSearchStub extends ReviewSearch {

    private final ReviewRepositoryStub repository = new ReviewRepositoryStub();

    public ReviewSearchStub(final List<ReviewSearchResult> initData) {

        super();
        initData.forEach(repository::addResult);
    }


    @Override
    public ReviewSearchResults searchNewReviews(final int lotPerCount, final int lotNumber) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();
        condition.setLotNumber(lotNumber);
        condition.setLotPerCount(lotPerCount);
        condition.setOrderType(ReviewSearchOrderType.PostTimeDesc);

        return searchData(condition);
    }

    @Override
    public ReviewSearchResults searchReviewByGenre(final GenreId genreId, final int lotPerCount, final int lotNumber) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();
        condition.setLotNumber(lotNumber);
        condition.setLotPerCount(lotPerCount);
        condition.getGenreIds().add(genreId);
        condition.setOrderType(ReviewSearchOrderType.PostTimeDesc);

        return searchData(condition);
    }

    @Override
    public Optional<ReviewSearchResult> searchByReviewId(final ReviewId reviewId) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();
        condition.setLotNumber(1);
        condition.setLotPerCount(1);
        condition.getReviewIds().add(reviewId);
        condition.setOrderType(ReviewSearchOrderType.PostTimeDesc);

        final ReviewSearchResults results = searchData(condition);
        final List<ReviewSearchResult > resultList = results.getResults();

        if (resultList.isEmpty()) {

            return Optional.empty();
        }

        return Optional.of(resultList.get(0));
    }

    private ReviewSearchResults searchData(final ReviewSearchCondition condition) {

        final List<ReviewSearchResult> searchResults = repository.search(condition);
        final int resultCount = repository.getSearchCount(condition);

        return new ReviewSearchResults(condition, searchResults, resultCount);
    }
}
