package jp.ne.glory.infra.db.game.dao;

import java.util.Optional;
import jp.ne.glory.infra.db.framework.DaoSettingAnnotation;
import jp.ne.glory.infra.db.game.entity.GameSiteUrl;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 * ゲームサイトURLDAO.
 *
 * @author Junki Yamad
 */
@Dao
@DaoSettingAnnotation
public interface GameSiteUrlDao {

    /**
     * IDをキーにゲームを取得する.
     *
     * @param gameId
     * @return ゲームサイトURL
     */
    @Select
    Optional<GameSiteUrl> selectById(final long gameId);

    /**
     * レコードをINSERTする.
     *
     * @param gameSite ゲームサイトURL
     * @return 登録件数
     */
    @Insert
    int insert(final GameSiteUrl gameSite);

    /**
     * レコードをUPDATEする.
     *
     * @param gameSite ゲームサイトURL
     * @return 更新件数
     */
    @Update
    int update(final GameSiteUrl gameSite);

    /**
     * レコードを削除する.
     *
     * @param gameId ゲームID
     * @return 削除件数
     */
    @Delete(sqlFile = true)
    int delete(final long gameId);
}
