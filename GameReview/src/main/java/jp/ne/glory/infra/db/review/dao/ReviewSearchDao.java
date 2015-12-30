package jp.ne.glory.infra.db.review.dao;

import java.util.List;
import java.util.Optional;
import jp.ne.glory.infra.db.framework.DaoSettingAnnotation;
import jp.ne.glory.infra.db.review.entity.ReviewInfo;
import jp.ne.glory.infra.db.review.entity.ReviewSearchParam;
import jp.ne.glory.infra.db.review.entity.ReviewSearchRow;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

/**
 * レビュー検索DAO.
 *
 * @author Junki Yamada
 */
@Dao
@DaoSettingAnnotation
public interface ReviewSearchDao {

    /**
     * レビューIDをキーにレビュー情報を取得する.
     *
     * @param reviewId レビューID
     * @return レビュー情報
     */
    @Select
    Optional<ReviewInfo> searchInfo(long reviewId);

    /**
     * レビューを検索する.
     *
     * @param condition 検索条件
     * @return レビュー検索結果リスト
     */
    @Select
    List<ReviewSearchRow> search(final ReviewSearchParam condition);
}
