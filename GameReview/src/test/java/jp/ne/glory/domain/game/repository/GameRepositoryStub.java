package jp.ne.glory.domain.game.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;
import jp.ne.glory.domain.genre.value.GenreId;

public class GameRepositoryStub implements GameRepository {

    private final Map<Long, Game> gameMap = new HashMap<>();

    private long sequence = 1;

    @Override
    public GameId save(final Game game) {

        final Game saveGame;
        if (game.getId() == null) {

            saveGame = new Game(new GameId(sequence), game.getTitle());
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

        if (condition.getLotPerCount() < 1) {

            return allList;
        }

        final int tempStartIndex = (condition.getLotNumber() - 1) * condition.getLotPerCount();

        final int startIndex = tempStartIndex < 1 ? 0 : tempStartIndex - 1;
        final int tempEndIndex = startIndex + condition.getLotPerCount();

        final int listLastIndex = allList.size() - 1;
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
}
