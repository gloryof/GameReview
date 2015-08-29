package jp.ne.glory.infra.db.repository.review;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import javax.enterprise.context.RequestScoped;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.repository.ReviewRepository;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;

/**
 * レビューリポジトリ.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class ReviewRepositoryImpl implements ReviewRepository {

    private static final Map<Long, ReviewSearchResult> reviewMap = new HashMap<>();

    private static long sequence = 1;

    static {

        final List<Genre> genreList = createBaseGenreList();
        LongStream.rangeClosed(1, 400)
                .mapToObj(i -> createSearchResult(i, genreList))
                .forEach(v -> {
                    reviewMap.put(v.getReview().getId().getValue(), v);
                });
    }

    @Override
    public ReviewId save(final Review review) {

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

        final Game stubGame = new Game(GameId.notNumberingValue());
        stubGame.setTitle(new Title("テスト"));
        final Genre stubGenre = new Genre(new GenreId(2l), new GenreName("テストジャンル"));
        final ReviewSearchResult result = new ReviewSearchResult(saveReview, stubGame, stubGenre);
        reviewMap.put(saveReview.getId().getValue(), result);

        return saveReview.getId();
    }

    public ReviewId save(final Review review, final Game game, final Genre genre) {

        final ReviewSearchResult result = new ReviewSearchResult(review, game, genre);
        reviewMap.put(review.getId().getValue(), result);

        return review.getId();
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

        if (resultList.isEmpty()) {

            return resultList;
        }

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

    private static List<Genre> createBaseGenreList() {

        return LongStream
                .rangeClosed(1, 3)
                .mapToObj(i -> new Genre(new GenreId(i), new GenreName("ジャンル" + i)))
                .collect(Collectors.toList());
    }

    private static ReviewSearchResult createSearchResult(final long number, final List<Genre> genreList) {

        final Game game = createTestGame(number);
        final Review review = createTestReview(number);
        review.setGameId(game.getId());

        final int genreIndex = (int) (number % genreList.size());
        final Genre genre = genreList.get(genreIndex);

        return new ReviewSearchResult(review, game, genre);
    }

    private static Game createTestGame(final long number) {

        final GameId gameId = new GameId(number);
        final Title title = new Title("ゲーム" + number);

        final Game game = new Game(gameId);
        final int ceroId = (int) number % 6;
        game.setTitle(title);
        game.setCeroRating(CeroRating.fromString(String.valueOf(ceroId)));
        game.setUrl(new SiteUrl("http//test.co.jp/" + number));

        return game;
    }

    private static Review createTestReview(final long number) {

        final Review review = new Review(new ReviewId(number));

        review.setPostTime(new PostDateTime(new DateTimeValue(LocalDateTime.now())));

        final StringBuilder goodPointValue = new StringBuilder();
        final StringBuilder badPointValue = new StringBuilder();
        final StringBuilder commentValue = new StringBuilder();

        final String lineSeparator = System.getProperty("line.separator");
        IntStream.rangeClosed(1, 10).forEach(v -> {
            goodPointValue.append("ああああああああああああああああああああああああああああああああああああ").append(lineSeparator);
            badPointValue.append("いいいいいいいいいいいいいいいいいいいいいいいいいいいいいいいいいいいい").append(lineSeparator);;
            commentValue.append("うううううううううううううううううううううううううううううううううううう").append(lineSeparator);;
        });

        review.setGoodPoint(new GoodPoint(goodPointValue.toString()));
        review.setBadPoint(new BadPoint(badPointValue.toString()));
        review.setComment(new Comment(commentValue.toString()));

        final Score score = new Score();
        score.setAddiction(ScorePoint.Point1);
        score.setLoadTime(ScorePoint.Point2);
        score.setMusic(ScorePoint.Point3);
        score.setOperability(ScorePoint.Point4);
        score.setStory(ScorePoint.Point5);
        review.setScore(score);

        return review;
    }
}
