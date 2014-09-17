package jp.ne.glory.domain.game.repository;

import java.util.Optional;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;

/**
 * ゲームリポジトリ.
 *
 * @author Junki Yamada
 */
public interface GameRepository {

    /**
     * ゲームを保存する.
     *
     * @param game ゲーム
     * @return 保存したゲームID
     */
    GameId save(final Game game);

    /**
     * ゲームIDでユーザを探す.
     *
     * @param gameId ゲームID
     * @return ゲーム
     */
    Optional<Game> findBy(final GameId gameId);
}
