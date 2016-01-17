package jp.ne.glory.test.review.search;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.LastUpdateDateTime;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;

public class ReviewSearchDataGenerator {

    public static List<ReviewSearchResult> createBaseSearchResults(final int dataCount) {

        return createBaseSearchResults(dataCount, LocalDateTime.now());
    }

    public static List<ReviewSearchResult> createBaseSearchResults(final int dataCount, final LocalDateTime postedBase) {

        return createBaseSearchResults(dataCount, createBaseGenreList(), postedBase);
    }

    public static List<ReviewSearchResult> createBaseSearchResults(final int dataCount, final List<Genre> genreList) {

        return createBaseSearchResults(dataCount, genreList, LocalDateTime.now());
    }


    public static List<ReviewSearchResult> createBaseSearchResults(final int dataCount, final List<Genre> genreList,
            final LocalDateTime postedBase) {

        return LongStream
                .rangeClosed(1, dataCount)
                .mapToObj(i -> createSearchResult(i, genreList, postedBase.plusDays(i)))
                .collect(Collectors.toList());
    }


    public static List<Genre> createBaseGenreList() {

        return LongStream
                .rangeClosed(1, 3)
                .mapToObj(i -> new Genre(new GenreId(i), new GenreName("ジャンル" + i)))
                .collect(Collectors.toList());
    }

    private static ReviewSearchResult createSearchResult(final long number, final List<Genre> genreList,
            final LocalDateTime postedDatetime) {

        final Game game = createTestGame(number);
        final Review review = createTestReview(number, postedDatetime);
        review.setGameId(game.getId());

        final int genreIndex = (int) (number % genreList.size());
        final Genre genre = genreList.get(genreIndex);

        return new ReviewSearchResult(review, game, genre);
    }

    private static Game createTestGame(final long number) {

        final GameId gameId = new GameId(number);
        final Title title = new Title("ゲーム" + number);

        final Game game = new Game(gameId);
        game.setTitle(title);

        return game;
    }

    private static Review createTestReview(final long number, final LocalDateTime postedDatetime) {

        final Review review = new Review(new ReviewId(number));

        review.setPostTime(new PostDateTime(new DateTimeValue(postedDatetime)));
        review.setLastUpdate(new LastUpdateDateTime(new DateTimeValue(postedDatetime)));

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
