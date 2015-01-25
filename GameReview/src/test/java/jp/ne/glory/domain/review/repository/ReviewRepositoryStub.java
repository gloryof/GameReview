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
        if (review.getId() == null) {

            saveReview = new Review(new ReviewId(sequence));
            saveReview.setBadPoint(review.getBadPoint());
            saveReview.setComment(review.getComment());
            saveReview.setGoodPoint(review.getGoodPoint());
            saveReview.setScore(review.getScore());

            sequence++;
        } else {

            saveReview = review;
        }

        final Game stubGame = new Game(GameId.notNumberingValue(), new Title("テスト"));
        final Genre stubGenre = new Genre(new GenreId(2l), new GenreName("テストジャンル"));
        final ReviewSearchResult result = new ReviewSearchResult(saveReview, stubGame, stubGenre);
        reviewMap.put(saveReview.getId().getValue(), result);

        return saveReview.getId();
    }

    public void addResult(final ReviewSearchResult result) {

        reviewMap.put(result.getReview().getId().getValue(), result);
    }

    @Override
    public Optional<Review> findBy(ReviewId reviewId) {

        final ReviewSearchResult result = reviewMap.get(reviewId.getValue());

        if (result == null) {

            return Optional.empty();
        }

        return Optional.ofNullable(result.getReview());
    }

    @Override
    public List<ReviewSearchResult> search(ReviewSearchCondition condition) {

        List<ReviewSearchResult> resultList = getSearchResult(condition, true);
        
        resultList.sort((x, y) -> {
            LocalDateTime xPostTime = x.getReview().getPostTime().getValue().getValue();
            LocalDateTime yPostTime = y.getReview().getPostTime().getValue().getValue();

            return xPostTime.compareTo(yPostTime);
        });

        if (0 < condition.getTargetCount()) {

            resultList = resultList.subList(0, condition.getTargetCount());
        }

        final int first = (condition.getLotNumber() - 1) * condition.getLotPerCount();
        final int last = condition.getLotNumber() * condition.getLotPerCount();

        return resultList.subList(first, last);
    }

    @Override
    public int getSearchCount(final ReviewSearchCondition condition) {
        return getSearchResult(condition, false).size();
    }

    private List<ReviewSearchResult> getSearchResult(final ReviewSearchCondition condition, final boolean limitedCount) {

        long maxCount = Long.MAX_VALUE;
        if (limitedCount && 0 < condition.getTargetCount()) {

            maxCount = condition.getTargetCount();
        }

        final List<ReviewSearchResult> resultList = reviewMap.entrySet().stream()
                .filter(entry -> isMatchSearchCondition(entry.getValue(), condition))
                .limit(maxCount)
                .map(v -> v.getValue())
                .collect(Collectors.toList());

        return resultList;
    }

    private boolean isMatchSearchCondition(final ReviewSearchResult result, final ReviewSearchCondition condition) {

        final List<ReviewId> reviewIds = condition.getReviewIds();
        final List<GenreId> genreIds = condition.getGenreIds();

        if (!reviewIds.isEmpty()) {

            final Set<Long> idSet = reviewIds
                    .stream()
                    .map(v -> v.getValue())
                    .collect(Collectors.toSet());

            if (!idSet.contains(result.getReview().getId().getValue())) {

                return false;
            }
        }

        if (!genreIds.isEmpty()) {

            final Set<Long> idSet = genreIds
                    .stream()
                    .map(v -> v.getValue())
                    .collect(Collectors.toSet());

            if (!idSet.contains(result.getGenre().getId().getValue())) {

                return false;
            }
        }

        return true;
    }
}
