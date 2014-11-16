package jp.ne.glory.domain.review.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private final Map<Long, Review> reviewMap = new HashMap<>();

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
        reviewMap.put(saveReview.id.value, saveReview);

        return saveReview.id;
    }

    @Override
    public Optional<Review> findBy(ReviewId reviewId) {

        return Optional.ofNullable(reviewMap.get(reviewId.value));
    }

    @Override
    public List<ReviewSearchResult> search(ReviewSearchCondition condition) {

        List<ReviewSearchResult> resultList = getSearchResult(condition);
        
        resultList.sort((x, y) -> {
            LocalDateTime xPostTime = x.review.postTime.getValue().getValue();
            LocalDateTime yPostTime = y.review.postTime.getValue().getValue();

            return xPostTime.compareTo(yPostTime);
        });

        if (0 < condition.targetCount) {

            resultList = resultList.subList(0, condition.targetCount);
        }

        return resultList;
    }

    @Override
    public int getSearchCount(ReviewSearchCondition condition) {
        return getSearchResult(condition).size();
    }

    private List<ReviewSearchResult> getSearchResult(ReviewSearchCondition condition) {

        final Game stubGame = new Game(GameId.notNumberingValue(), new Title("テスト"));
        final Genre stubGenre = new Genre(new GenreId(2l), new GenreName("テストジャンル"));
        final List<ReviewSearchResult> resultList = reviewMap.entrySet().stream()
                .filter(entry -> true)
                .map(entry -> new ReviewSearchResult(entry.getValue(), stubGame, stubGenre))
                .collect(Collectors.toList());

        return resultList;
    }
}
