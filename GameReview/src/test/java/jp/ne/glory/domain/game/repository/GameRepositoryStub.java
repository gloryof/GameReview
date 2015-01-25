package jp.ne.glory.domain.game.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;

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

}
