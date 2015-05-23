package jp.ne.glory.domain.game.repository;

import java.util.List;
import java.util.Optional;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;

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

    /**
     * 全てのゲームを取得する.
     *
     * @return ゲームリスト
     */
    List<Game> findAll();

    /**
     * ゲームを検索する.
     *
     * @param condition 検索条件
     * @return 検索結果
     */
    List<Game> search(GameSearchCondition condition);

    /**
     * ゲームの検索結果件数を取得する.
     *
     * @param condition 検索条件
     * @return 検索結果件数
     */
    int getSearchCount(GameSearchCondition condition);

}
