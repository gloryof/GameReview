package jp.ne.glory.infra.db.user.dao;

import java.util.Optional;
import jp.ne.glory.infra.db.framework.DaoSettingAnnotation;
import jp.ne.glory.infra.db.user.entity.UserAccount;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 * ユーザアカウントDAO.
 *
 * @author Junki Yamada
 */
@Dao
@DaoSettingAnnotation
public interface UserAccountDao {

    /**
     *
     * IDをキーにユーザアカウントを取得する.
     *
     * @param id ID
     * @return ユーザアカウント
     */
    @Select
    Optional<UserAccount> selectById(final Long id);

    /**
     * ログインIDをキーにユーザアカウントを取得する.
     *
     * @param loginId ログインID
     * @return ユーザアカウント
     */
    @Select
    Optional<UserAccount> selectByLoginId(final String loginId);

    /**
     * レコードををINSERTする.
     *
     * @param userAccount ユーザ情報
     * @return 更新件数
     */
    @Insert
    int insert(final UserAccount userAccount);

    /**
     * レコードををUPDATEする.
     *
     * @param userAccount ユーザ情報
     * @return 更新件数
     */
    @Update
    int update(final UserAccount userAccount);
}
