package jp.ne.glory.infra.db.game.dao;

import java.util.List;
import java.util.Optional;
import jp.ne.glory.infra.db.framework.DaoSettingAnnotation;
import jp.ne.glory.infra.db.game.entity.GameBaseInfo;
import jp.ne.glory.infra.db.game.entity.GameInfo;
import jp.ne.glory.infra.db.game.entity.GameSearchParam;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 * ゲームDAO.
 *
 * @author Junki Yamada
 */
@Dao
@DaoSettingAnnotation
public interface GameBaseInfoDao {

    /**
     * IDをキーにゲームEntityを取得する.
     *
     * @param gameId
     * @return ゲームEntity
     */
    @Select
    Optional<GameBaseInfo> selectEntytById(final long gameId);

    /**
     * IDをキーにゲームを取得する.
     *
     * @param gameId
     * @return ゲーム
     */
    @Select
    Optional<GameInfo> selectById(final long gameId);

    /**
     * 全てのゲームを取得する.
     *
     * @return ゲームリスト
     */
    @Select
    List<GameInfo> selectAll();

    /**
     * ゲームを検索する.
     *
     * @param condition 検索条件
     * @return ゲームリスト
     */
    @Select
    List<GameInfo> search(final GameSearchParam condition);

    /**
     * シーケンスの値を取得する.
     *
     * @return シーケンス値
     */
    @Select
    long getSequence();

    /**
     * レコードをINSERTする.
     *
     * @param game ゲーム
     * @return 登録件数
     */
    @Insert
    int insert(final GameBaseInfo game);

    /**
     * レコードをUPDATEする.
     *
     * @param game ゲーム
     * @return 更新件数
     */
    @Update
    int update(final GameBaseInfo game);
}
