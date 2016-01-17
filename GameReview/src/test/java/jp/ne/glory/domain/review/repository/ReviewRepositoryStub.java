package jp.ne.glory.domain.review.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jp.ne.glory.common.type.DateValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;

public class ReviewRepositoryStub implements ReviewRepository {

    private final Map<Long, ReviewSearchResult> reviewMap = new HashMap<>();

    private long sequence = 1;

    @Override
    public ReviewId save(final Review review) {

        final Game stubGame = new Game(GameId.notNumberingValue());
        stubGame.setTitle(new Title("テスト"));

        final Genre stubGenre = new Genre(new GenreId(2l), new GenreName("テストジャンル"));

        return save(review, stubGame, stubGenre);
    }

    public ReviewId save(final Review review, final Game game, final Genre genre) {

        final Review saveReview;
        if (review.getId() == null || !review.isRegistered()) {

            saveReview = new Review(new ReviewId(sequence));
            saveReview.setBadPoint(review.getBadPoint());
            saveReview.setComment(review.getComment());
            saveReview.setGoodPoint(review.getGoodPoint());
            saveReview.setScore(review.getScore());
            saveReview.setPostTime(review.getPostTime());
            saveReview.setLastUpdate(review.getLastUpdate());

            sequence++;
        } else {

            saveReview = review;
        }

        final ReviewSearchResult result = new ReviewSearchResult(saveReview, game, genre);
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

        if (condition.getLotPerCount() < 1) {

            return resultList;
        }

        final int first = (condition.getLotNumber() - 1) * condition.getLotPerCount();
        final int last = condition.getLotNumber() * condition.getLotPerCount();

        if (resultList.isEmpty()) {

            return resultList;
        }

        return resultList.subList(first, last);
    }

    @Override
    public int getSearchCount(final ReviewSearchCondition condition) {
        return getSearchResult(condition, false).size();
    }

    public long getCurrentSequence() {

        return sequence;
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
        if (!reviewIds.isEmpty()) {

            final Set<Long> idSet = reviewIds
                    .stream()
                    .map(v -> v.getValue())
                    .collect(Collectors.toSet());

            if (!idSet.contains(result.getReview().getId().getValue())) {

                return false;
            }
        }

        final List<GenreId> genreIds = condition.getGenreIds();
        if (!genreIds.isEmpty()) {

            final Set<Long> idSet = genreIds
                    .stream()
                    .map(v -> v.getValue())
                    .collect(Collectors.toSet());

            if (!idSet.contains(result.getGenre().getId().getValue())) {

                return false;
            }
        }

        final DateValue from = condition.getPostedFrom();
        if (from != null) {

            final PostDateTime postDatetime = result.getReview().getPostTime();
            final LocalDateTime datetime = postDatetime.getValue().getValue();
            final LocalDate date = datetime.toLocalDate();

            if (!date.isEqual(from.getValue()) && !date.isAfter(from.getValue())) {

                return false;
            }
        }

        final DateValue to = condition.getPostedFrom();
        if (to != null) {

            final PostDateTime postDatetime = result.getReview().getPostTime();
            final LocalDateTime datetime = postDatetime.getValue().getValue();
            final LocalDate date = datetime.toLocalDate();

            if (!date.isEqual(to.getValue()) && !date.isAfter(to.getValue())) {

                return false;
            }
        }

        return true;
    }
}
