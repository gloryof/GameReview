package jp.ne.glory.domain.review.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;

public class ReviewRepositoryStub implements ReviewRepository {

    private final Map<Long, ReviewSearchResult> reviewMap = new HashMap<>();

    private long sequence = 1;

    @Override
    public ReviewId save(Review review) {

        final Review saveReview;
        if (review.id == null) {

            saveReview = new Review(new ReviewId(sequence));
            saveReview.badPoint = review.badPoint;
            saveReview.comment = review.comment;
            saveReview.gooodPoint = review.gooodPoint;
            saveReview.score = review.score;

            sequence++;
        } else {

            saveReview = review;
        }

        final Game stubGame = new Game(GameId.notNumberingValue(), new Title("テスト"));
        final Genre stubGenre = new Genre(new GenreId(2l), new GenreName("テストジャンル"));
        final ReviewSearchResult result = new ReviewSearchResult(saveReview, stubGame, stubGenre);
        reviewMap.put(saveReview.id.value, result);

        return saveReview.id;
    }

    public void addResult(final ReviewSearchResult result) {

        reviewMap.put(result.review.id.value, result);
    }

    @Override
    public Optional<Review> findBy(ReviewId reviewId) {

        final ReviewSearchResult result = reviewMap.get(reviewId.value);

        if (result == null) {

            return Optional.empty();
        }

        return Optional.ofNullable(result.review);
    }

    @Override
    public List<ReviewSearchResult> search(ReviewSearchCondition condition) {

        List<ReviewSearchResult> resultList = getSearchResult(condition, true);
        
        resultList.sort((x, y) -> {
            LocalDateTime xPostTime = x.review.postTime.getValue().getValue();
            LocalDateTime yPostTime = y.review.postTime.getValue().getValue();

            return xPostTime.compareTo(yPostTime);
        });

        if (0 < condition.targetCount) {

            resultList = resultList.subList(0, condition.targetCount);
        }

        final int first = (condition.lotNumber - 1) * condition.lotPerCount;
        final int last = condition.lotNumber * condition.lotPerCount;

        return resultList.subList(first, last);
    }

    @Override
    public int getSearchCount(final ReviewSearchCondition condition) {
        return getSearchResult(condition, false).size();
    }

    private List<ReviewSearchResult> getSearchResult(final ReviewSearchCondition condition, final boolean limitedCount) {

        long maxCount = Long.MAX_VALUE;
        if (limitedCount && 0 < condition.targetCount) {

            maxCount = condition.targetCount;
        }

        final List<ReviewSearchResult> resultList = reviewMap.entrySet().stream()
                .filter(entry -> isMatchSearchCondition(entry.getValue(), condition))
                .limit(maxCount)
                .map(v -> v.getValue())
                .collect(Collectors.toList());

        return resultList;
    }

    private boolean isMatchSearchCondition(final ReviewSearchResult result, final ReviewSearchCondition condition) {

        final List<ReviewId> reviewIds = condition.reviewIds;
        final List<GenreId> genreIds = condition.genreIds;

        if (!reviewIds.isEmpty()) {

            final Set<Long> idSet = reviewIds
                    .stream()
                    .map(v -> v.value)
                    .collect(Collectors.toSet());

            if (!idSet.contains(result.review.id.value)) {

                return false;
            }
        }

        if (!genreIds.isEmpty()) {

            final Set<Long> idSet = genreIds
                    .stream()
                    .map(v -> v.value)
                    .collect(Collectors.toSet());

            if (!idSet.contains(result.genre.id.value)) {

                return false;
            }
        }

        return true;
    }
}
