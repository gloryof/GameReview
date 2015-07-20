package jp.ne.glory.infra.db.repository.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import javax.enterprise.context.RequestScoped;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepository;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;
import jp.ne.glory.domain.genre.value.GenreId;

/**
 * ゲームリポジトリ.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class GameRepositoryImpl implements GameRepository {

    private static final Map<Long, Game> gameMap = new HashMap<>();

    private static long sequence = 1;

    static {

        LongStream.rangeClosed(1, 250)
                .mapToObj(GameRepositoryImpl::createGame)
                .forEach(v -> gameMap.put(v.getId().getValue(), v));
        sequence = 50;
    }

    @Override
    public GameId save(final Game game) {

        final Game saveGame;
        if (game.getId() == null || !game.isRegistered()) {

            saveGame = new Game(new GameId(sequence));
            saveGame.setTitle(game.getTitle());
            saveGame.setCeroRating(game.getCeroRating());
            saveGame.setGenreId(game.getGenreId());
            saveGame.setUrl(game.getUrl());

            sequence++;
        } else {

            saveGame = game;
        }
        gameMap.put(saveGame.getId().getValue(), saveGame);

        return saveGame.getId();
    }

    @Override
    public Optional<Game> findBy(final GameId gameId) {

        return Optional.ofNullable(gameMap.get(gameId.getValue()));
    }

    @Override
    public List<Game> findAll() {
        return gameMap.entrySet()
                .stream()
                .map(entry -> entry.getValue())
                .sorted((b, n) -> b.getId().getValue().compareTo(n.getId().getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Game> search(final GameSearchCondition condition) {

        final List<Game> allList = searchAll(condition);

        allList.sort((v1, v2) -> {

            final long sortedValue = v2.getId().getValue() - v1.getId().getValue();
            return (int) sortedValue;
        });

        if (condition.getLotPerCount() < 1) {

            return allList;
        }

        final int tempStartIndex = (condition.getLotNumber() - 1) * condition.getLotPerCount();

        final int startIndex = tempStartIndex < 1 ? 0 : tempStartIndex - 1;
        final int tempEndIndex = startIndex + condition.getLotPerCount();

        final int listLastIndex = allList.size();
        final int endIndex = listLastIndex < tempEndIndex ? listLastIndex : tempEndIndex;

        return allList.subList(startIndex, endIndex);
    }

    private List<Game> searchAll(final GameSearchCondition condition) {
        return gameMap.entrySet().stream()
                .map(entry -> entry.getValue())
                .filter(v -> isMatchCondition(condition, v))
                .collect(Collectors.toList());
    }

    @Override
    public int getSearchCount(final GameSearchCondition condition) {

        return searchAll(condition).size();
    }

    private boolean isMatchCondition(final GameSearchCondition condition, final Game game) {

        final Title title = condition.getTitle();

        if (title != null && !title.getValue().isEmpty()) {

            if (!title.getValue().equals(game.getTitle().getValue())) {

                return false;
            }
        }

        final CeroRating rating = condition.getCeroRating();

        if (rating != null) {

            if (!rating.equals(game.getCeroRating())) {

                return false;
            }
        }

        final GenreId genreId = condition.getGenreId();
        if (genreId != null) {

            if (!genreId.isSame(game.getGenreId())) {

                return false;
            }
        }

        return true;
    }

    private static Game createGame(final long paramGameId) {

        final GameId gameId = new GameId(paramGameId);

        final Game game = new Game(gameId);

        final Title title = new Title("タイトル" + paramGameId);
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
