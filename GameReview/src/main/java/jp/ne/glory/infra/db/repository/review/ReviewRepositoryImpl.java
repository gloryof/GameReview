package jp.ne.glory.infra.db.repository.review;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import javax.enterprise.context.RequestScoped;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.repository.ReviewRepository;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;

/**
 * レビューリポジトリ.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class ReviewRepositoryImpl implements ReviewRepository {

    private static final List<ReviewSearchResult> stubResults = new ArrayList<>();

    static {

        LongStream.rangeClosed(1, 10).forEach(v -> {

            final ReviewId reviewId = new ReviewId(100 + v);
            final GameId gameId = new GameId(200 + v);

            final Review review = new Review(reviewId);
            review.setPostTime(new PostDateTime(new DateTimeValue(LocalDateTime.now())));

            final Game game = new Game(gameId, new Title("テスト" + v));
            final long genreId = (v % 3) + 1;
            final Genre genre = new Genre(new GenreId(genreId), new GenreName("テストジャンル"));

            stubResults.add(new ReviewSearchResult(review, game, genre));
        });
    }

    @Override
    public ReviewId save(Review review) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<Review> findBy(ReviewId reviewId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ReviewSearchResult> search(ReviewSearchCondition condition) {

        return searchStub(condition);
    }

    @Override
    public int getSearchCount(ReviewSearchCondition condition) {
        return searchStub(condition).size();
    }

    private List<ReviewSearchResult> searchStub(final ReviewSearchCondition condition) {

        List<ReviewSearchResult> returnList = stubResults
                .stream()
                .filter(v -> isMatch(condition, v))
                .collect(Collectors.toList());

        if (returnList.size() < condition.getTargetCount()) {

            return returnList;
        }

        if (1 < condition.getTargetCount()) {

            returnList = returnList.subList(0, condition.getTargetCount());
        }

        return returnList;
    }

    private boolean isMatch(final ReviewSearchCondition condition, final ReviewSearchResult result) {

        if (!isMatchReviewIdStub(condition, result)) {

            return false;
        }

        return (!isMatchGenreIdStub(condition, result));
    }

    private boolean isMatchReviewIdStub(final ReviewSearchCondition condition, final ReviewSearchResult result) {

        final List<ReviewId> reviewIds = condition.getReviewIds();
        if (reviewIds.isEmpty()) {

            return true;
        }

        final Optional<ReviewId> option = reviewIds.stream().filter(v -> v.isSame(result.getReview().getId())).findAny();

        return option.isPresent();
    }

    private boolean isMatchGenreIdStub(final ReviewSearchCondition condition, final ReviewSearchResult result) {

        final List<GenreId> genreIds = condition.getGenreIds();
        if (genreIds.isEmpty()) {

            return true;
        }

        final Optional<GenreId> option = genreIds.stream().filter(v -> v.isSame(result.getGenre().getId())).findAny();

        return option.isPresent();
    }
}
