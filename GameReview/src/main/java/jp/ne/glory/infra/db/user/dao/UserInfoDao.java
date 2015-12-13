package jp.ne.glory.infra.db.user.dao;

import java.util.List;
import java.util.Optional;
import jp.ne.glory.infra.db.framework.DaoSettingAnnotation;
import jp.ne.glory.infra.db.user.entity.UserInfo;
import jp.ne.glory.infra.db.user.entity.UserListResult;
import jp.ne.glory.infra.db.user.entity.UserSearchParam;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 * ユーザ情報DAO.
 *
 * @author Junki Yamada
 */
@Dao
@DaoSettingAnnotation
public interface UserInfoDao {

    /**
     * IDをキーにユーザ情報を取得する.
     *
     * @param id ID
     * @return ユーザ情報
     */
    @Select
    Optional<UserInfo> selectById(final Long id);

    /**
     * レコードををINSERTする.
     *
     * @param user ユーザ情報
     * @return 更新件数
     */
    @Insert
    int insert(final UserInfo user);

    /**
     * レコードををUPDATEする.
     *
     * @param user ユーザ情報
     * @return 更新件数
     */
    @Update
    int update(final UserInfo user);

    /**
     * シーケンスの値を取得する.
     *
     * @return シーケンス値
     */
    @Select
    long getSequence();

    /**
     * ユーザ情報のリストを取得する.
     *
     * @return ユーザ情報リスト
     */
    @Select
    List<UserListResult> getListResult();

    /**
     * ユーザ情報を検索する.
     *
     * @param condition 検索条件
     * @return ユーザ情報リスト
     */
    @Select
    List<UserListResult> search(final UserSearchParam condition);
}
