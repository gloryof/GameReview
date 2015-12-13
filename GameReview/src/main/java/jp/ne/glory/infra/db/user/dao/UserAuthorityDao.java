package jp.ne.glory.infra.db.user.dao;

import java.util.List;
import jp.ne.glory.infra.db.framework.DaoSettingAnnotation;
import jp.ne.glory.infra.db.user.entity.UserAuthority;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;

/**
 * ユーザ権限情報DAO.
 *
 * @author Junki Yamada
 */
@Dao
@DaoSettingAnnotation
public interface UserAuthorityDao {

    /**
     *
     * ユーザIDをキーに権限情報リストを取得する.
     *
     * @param userId ユーザID
     * @return 権限リスト
     */
    @Select
    List<UserAuthority> selectByUserId(final Long userId);

    /**
     * 権限情報を一括登録する.
     *
     * @param authorities 権限リスト
     * @return 更新件数
     */
    @BatchInsert
    int[] batchInsert(final List<UserAuthority> authorities);

    /**
     * ユーザIDをキーに削除する.
     *
     * @param userId ユーザID
     */
    @Delete(sqlFile = true)
    int deleteByUserId(final Long userId);
}
