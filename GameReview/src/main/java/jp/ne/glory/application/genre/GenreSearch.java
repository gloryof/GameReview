package jp.ne.glory.application.genre;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepository;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.search.GenreSearchCondition;
import jp.ne.glory.domain.genre.value.search.GenreSearchResults;

/**
 * ジャンル検索に関する処理.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class GenreSearch {

    /**
     * ジャンルリポジトリ.
     */
    private final GenreRepository repository;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    @Deprecated
    GenreSearch() {

        this.repository = null;
    }

    /**
     * コンストラクタ.
     *
     * @param repository リポジトリ
     */
    @Inject
    public GenreSearch(final GenreRepository repository) {

        this.repository = repository;
    }

    /**
     * 全てのジャンルを取得する.
     *
     * @return ジャンルリスト
     */
    public List<Genre> getAllGenres() {

        return repository.getAllGenreList();
    }

    /**
     * ジャンルIDで検索する.
     *
     * @param genreId ジャンルID
     * @return ジャンル
     */
    public Optional<Genre> searchBy(final GenreId genreId) {

        return repository.findBy(genreId);
    }

    /**
     * ジャンルの検索を行う.
     *
     * @param condition 検索条件
     * @return 検索結果
     */
    public GenreSearchResults search(final GenreSearchCondition condition) {

        final List<Genre> genreList = repository.search(condition);
        final int resultCount = repository.getSearchCount(condition);

        return new GenreSearchResults(condition, genreList, resultCount);
    }
}
