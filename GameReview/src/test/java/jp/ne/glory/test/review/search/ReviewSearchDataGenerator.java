package jp.ne.glory.test.review.search;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;

public class ReviewSearchDataGenerator {

    public static List<ReviewSearchResult> createBaseSearchResults(final int dataCount) {

        return createBaseSearchResults(dataCount, createBaseGenreList());
    }

    public static List<ReviewSearchResult> createBaseSearchResults(final int dataCount, final List<Genre> genreList) {

        return LongStream
                .rangeClosed(1, dataCount)
                .mapToObj(i -> createSearchResult(i, genreList))
                .collect(Collectors.toList());
    }


    public static List<Genre> createBaseGenreList() {

        return LongStream
                .rangeClosed(1, 3)
                .mapToObj(i -> new Genre(new GenreId(i), new GenreName("ジャンル" + i)))
                .collect(Collectors.toList());
    }

    private static ReviewSearchResult createSearchResult(final long number, final List<Genre> genreList) {

        final Game game = createTestGame(number);
        final Review review = createTestReview(number);

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

    private static Review createTestReview(final long number) {

        final Review review = new Review(new ReviewId(number));

        review.setPostTime(new PostDateTime(new DateTimeValue(LocalDateTime.now())));

        return review;
    }
}
