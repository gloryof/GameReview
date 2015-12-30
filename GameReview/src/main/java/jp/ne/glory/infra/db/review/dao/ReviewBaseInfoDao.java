package jp.ne.glory.infra.db.review.dao;

import jp.ne.glory.infra.db.framework.DaoSettingAnnotation;
import jp.ne.glory.infra.db.review.entity.ReviewBaseInfo;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 * レビュー基本情報DAO.
 *
 * @author Junki Yamada
 */
@Dao
@DaoSettingAnnotation
public interface ReviewBaseInfoDao {

    /**
     * シーケンスの値を取得する.
     *
     * @return シーケンス値
     */
    @Select
    long getSequence();

    /**
     * レビューIDをキーにレビュー基本情報を取得する.
     *
     * @param reviewId レビューID
     * @return レビュー基本情報
     */
    @Select
    ReviewBaseInfo selectById(final long reviewId);

    /**
     * レコードをINSERTする.
     *
     * @param baseInfo レビュー情報
     * @return 登録件数
     */
    @Insert
    int insert(final ReviewBaseInfo baseInfo);

    /**
     * レコードをUPDATEする.
     *
     * @param baseInfo レビュー情報
     * @return 更新件数
     */
    @Update
    int update(final ReviewBaseInfo baseInfo);
}
