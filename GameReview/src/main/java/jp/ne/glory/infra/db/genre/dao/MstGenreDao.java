package jp.ne.glory.infra.db.genre.dao;

import java.util.List;
import java.util.Optional;
import jp.ne.glory.infra.db.framework.DaoSettingAnnotation;
import jp.ne.glory.infra.db.repository.entity.GenreSearchParam;
import jp.ne.glory.infra.db.repository.entity.MstGenre;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 * ジャンルマスタDAO.
 *
 * @author Junki Yamada
 */
@Dao
@DaoSettingAnnotation
public interface MstGenreDao {

    /**
     * IDをキーにジャンルを取得する.
     *
     * @param genreId
     * @return ジャンル
     */
    @Select
    Optional<MstGenre> selectById(final long genreId);

    /**
     * 全てのジャンルを取得する.
     *
     * @return ジャンルリスト
     */
    @Select
    List<MstGenre> selectAll();

    /**
     * ジャンルの検索を行う.
     *
     * @param condition 検索条件
     * @return ジャンルリスト
     */
    @Select
    List<MstGenre> search(final GenreSearchParam condition);

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
     * @param genre ジャンル
     * @return 登録件数
     */
    @Insert
    int insert(final MstGenre genre);

    /**
     * レコードをUPDATEする.
     *
     * @param genre ジャンル
     * @return 更新件数
     */
    @Update
    int update(final MstGenre genre);
}
