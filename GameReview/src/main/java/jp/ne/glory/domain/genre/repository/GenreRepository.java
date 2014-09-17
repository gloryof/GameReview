package jp.ne.glory.domain.genre.repository;

import java.util.Optional;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;

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
     * ジャンルIDでユーザを探す.
     *
     * @param genreId ジャンルID
     * @return ジャンル
     */
    Optional<Genre> findBy(final GenreId genreId);
}
