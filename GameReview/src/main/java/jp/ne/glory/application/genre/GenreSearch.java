package jp.ne.glory.application.genre;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepository;

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
}
