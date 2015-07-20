package jp.ne.glory.test.game.search;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.value.GenreId;

public class GameSearchDataGenerator {

    public static List<Game> creaeteGames(final int count) {

        return LongStream.rangeClosed(1, count)
                .mapToObj(v -> createGame(v))
                .collect(Collectors.toList());
    }

    private static Game createGame(final long paramGameId) {

        final GameId gameId = new GameId(paramGameId);
        final Title title = new Title("タイトル" + paramGameId);

        final Game game = new Game(gameId);
        game.setTitle(title);

        game.setUrl(new SiteUrl("http://localhost:8080/test/" + paramGameId));

        final CeroRating[] ratings = CeroRating.values();
        final int ratingIndex = (int) ((paramGameId - 1) % ratings.length);

        game.setCeroRating(ratings[ratingIndex]);

        final long genreIdValue = (paramGameId % 3) + 1;

        game.setGenreId(new GenreId(genreIdValue));

        return game;
    }
}
