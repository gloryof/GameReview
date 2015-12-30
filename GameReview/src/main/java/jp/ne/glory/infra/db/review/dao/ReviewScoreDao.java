package jp.ne.glory.infra.db.review.dao;

import jp.ne.glory.infra.db.framework.DaoSettingAnnotation;
import jp.ne.glory.infra.db.review.entity.ReviewScore;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 * レビュー点数DAO.
 *
 * @author Junki Yamada
 */
@Dao
@DaoSettingAnnotation
public interface ReviewScoreDao {

    /**
     * レビューIDをキーにレビュースコアを取得する.
     *
     * @param reviewId レビューID
     * @return レビュースコア
     */
    @Select
    ReviewScore selectById(final long reviewId);

    /**
     * レコードをINSERTする.
     *
     * @param baseInfo スコア情報
     * @return 登録件数
     */
    @Insert
    int insert(final ReviewScore baseInfo);

    /**
     * レコードをUPDATEする.
     *
     * @param baseInfo スコア情報
     * @return 更新件数
     */
    @Update
    int update(final ReviewScore baseInfo);
}
