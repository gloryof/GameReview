package jp.ne.glory.domain.genre.repository;

import java.util.List;
import java.util.Optional;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.search.GenreSearchCondition;

/**
 * ジャンルリポジトリ.
 *
 * @author Junki Yamada
 */
public interface GenreRepository {

    /**
     * ジャンルを保存する.
     *
     * @param genre ジャンル
     * @return 保存したジャンルID
     */
    GenreId save(final Genre genre);

    /**
     * ジャンルIDでジャンルを探す.
     *
     * @param genreId ジャンルID
     * @return ジャンル
     */
    Optional<Genre> findBy(final GenreId genreId);

    /**
     * 全てのジャンルを取得する.
     *
     * @return ジャンルリスト
     */
    List<Genre> getAllGenreList();

    /**
     * ジャンル検索.
     *
     * @param condition 検索条件
     * @return ジャンルリスト
     */
    List<Genre> search(final GenreSearchCondition condition);

    /**
     * ジャンルの検索結果件数を取得する.
     *
     * @param condition 検索条件
     * @return 検索結果件数
     */
    int getSearchCount(final GenreSearchCondition condition);
}
