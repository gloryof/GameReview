package jp.ne.glory.infra.db.genre.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepository;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.genre.value.search.GenreSearchCondition;
import jp.ne.glory.infra.db.genre.dao.MstGenreDao;
import jp.ne.glory.infra.db.genre.entity.GenreSearchParam;
import jp.ne.glory.infra.db.genre.entity.MstGenre;

/**
 * ジャンルリポジトリ.
 *
 * @author Junki Yamada
 */
@ApplicationScoped
public class GenreRepositoryImpl implements GenreRepository {

    /**
     * ジャンルマスタDAO.
     */
    private final MstGenreDao dao;

    /**
     * コンストラクタ.
     *
     * @param dao ジャンルマスタDAO
     */
    @Inject
    public GenreRepositoryImpl(final MstGenreDao dao) {

        this.dao = dao;
    }

    /**
     * ジャンルを保存する.
     *
     * @param genre ジャンル
     * @return
     */
    @Override
    public GenreId save(Genre genre) {

        if (genre.isRegistered()) {

            return new GenreId(updateGenre(genre));
        }

        return new GenreId(insertGenre(genre));
    }

    /**
     * ジャンルIDでジャンルを探す.
     *
     * @param genreId ジャンルID
     * @return ジャンル
     */
    @Override
    public Optional<Genre> findBy(GenreId genreId) {

        return dao.selectById(genreId.getValue())
                .map(v -> Optional.of(this.convertToDomainEntity(v)))
                .orElse(Optional.empty());
    }

    /**
     * 全ジャンルを取得する.
     *
     * @return ジャンルリスト
     */
    @Override
    public List<Genre> findAll() {

        return dao.selectAll().stream()
                .map(this::convertToDomainEntity)
                .collect(Collectors.toList());
    }

    /**
     * ジャンルの検索を行う.
     *
     * @param condition 検索条件
     * @return ジャンルリスト
     */
    @Override
    public List<Genre> search(final GenreSearchCondition condition) {

        return dao.search(new GenreSearchParam(condition)).stream()
                .map(this::convertToDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int getSearchCount(final GenreSearchCondition condition) {
        return search(condition).size();
    }

    /**
     * DBのエンティティからドメインのエンティティに変換する.
     *
     * @param dbEntity DBエンティティ
     * @return ドメインのエンティティ
     */
    private Genre convertToDomainEntity(final MstGenre dbEntity) {

        return new Genre(new GenreId(dbEntity.getGenreId()), new GenreName(dbEntity.getGenreName()));
    }

    /**
     * mst_genreのレコードをUPDATEする.
     *
     * @param genre ジャンル
     * @return ジャンルID
     */
    private long updateGenre(final Genre genre) {

        final long genreIdValue = genre.getId().getValue();
        final MstGenre entity = dao.selectById(genreIdValue).get();
        entity.setGenreName(genre.getName().getValue());
        entity.setLockUpdateTimestamp(LocalDateTime.now());

        dao.update(entity);

        return genreIdValue;
    }

    /**
     * mst_genreにレコードをINSERTする.
     *
     * @param genre ジャンル
     * @return ジャンルID
     */
    private long insertGenre(final Genre genre) {

        final long genreId = dao.getSequence();
        final MstGenre entity = new MstGenre();

        entity.setGenreId(genreId);
        entity.setGenreName(genre.getName().getValue());
        entity.setLockUpdateTimestamp(LocalDateTime.now());

        dao.insert(entity);

        return genreId;
    }
}
