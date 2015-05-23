package jp.ne.glory.application.game;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepository;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;
import jp.ne.glory.domain.game.value.search.GameSearchResults;

/**
 * ゲーム検索.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class GameSearch {

    /**
     * リポジトリ.
     */
    private final GameRepository repository;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    @Deprecated
    protected GameSearch() {

        this.repository = null;
    }

    /**
     * コンストラクタ.
     *
     * @param repository リポジトリ
     */
    @Inject
    public GameSearch(final GameRepository repository) {

        this.repository = repository;
    }

    /**
     * 全てのゲームを取得する
     *
     * @return ゲームリスト
     */
    public List<Game> getAllGames() {

        return repository.findAll();
    }

    /**
     * ゲームIDでゲームを検索する.
     *
     * @param gameId ゲームID
     * @return ゲーム
     */
    public Optional<Game> searchBy(GameId gameId) {

        return repository.findBy(gameId);
    }

    /**
     * ゲームを検索する.
     *
     * @param condition 検索条件
     * @return ゲーム
     */
    public GameSearchResults search(GameSearchCondition condition) {

        final List<Game> gameList = repository.search(condition);
        final int resultCount = repository.getSearchCount(condition);

        return new GameSearchResults(condition, gameList, resultCount);
    }
}
