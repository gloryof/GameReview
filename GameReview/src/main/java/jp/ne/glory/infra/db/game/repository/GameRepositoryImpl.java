package jp.ne.glory.infra.db.game.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepository;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.infra.db.game.dao.GameBaseInfoDao;
import jp.ne.glory.infra.db.game.dao.GameSiteUrlDao;
import jp.ne.glory.infra.db.game.entity.GameBaseInfo;
import jp.ne.glory.infra.db.game.entity.GameInfo;
import jp.ne.glory.infra.db.game.entity.GameSearchParam;
import jp.ne.glory.infra.db.game.entity.GameSiteUrl;

/**
 * ゲームリポジトリ.
 *
 * @author Junki Yamada
 */
@ApplicationScoped
public class GameRepositoryImpl implements GameRepository {

    /**
     * ゲームDAO.
     */
    private final GameBaseInfoDao gameBaseInfoDao;

    /**
     * ゲームサイトURLDAO.
     */
    private final GameSiteUrlDao gameSiteUrlDao;

    /**
     * コンストラクタ.
     *
     * @param gameBaseInfoDao ゲームDAO
     * @param gameSiteUrlDao ゲームサイトURLDAO
     */
    @Inject
    public GameRepositoryImpl(final GameBaseInfoDao gameBaseInfoDao, final GameSiteUrlDao gameSiteUrlDao) {

        this.gameBaseInfoDao = gameBaseInfoDao;
        this.gameSiteUrlDao = gameSiteUrlDao;
    }

    /**
     * ゲームを保存する.
     *
     * @param game ゲーム
     * @return ゲームID
     */
    @Override
    public GameId save(final Game game) {

        final long gameIdValue;
        if (game.isRegistered()) {

            gameIdValue = updateGame(game);
        } else {

            gameIdValue = insertGame(game);
        }

        return new GameId(gameIdValue);
    }

    /**
     * ゲームIDをキーに検索を行う.
     *
     * @param gameId ゲームID
     * @return ゲーム
     */
    @Override
    public Optional<Game> findBy(final GameId gameId) {

        return gameBaseInfoDao.selectById(gameId.getValue())
                .map(this::convertToEntity);
    }

    /**
     * 全てのゲームを取得する.
     *
     * @return ゲームリスト
     */
    @Override
    public List<Game> findAll() {
        return gameBaseInfoDao.selectAll().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    /**
     * ゲームの検索を行う.
     *
     * @param condition 検索条件
     * @return ゲームリスト
     */
    @Override
    public List<Game> search(final GameSearchCondition condition) {

        return gameBaseInfoDao.search(new GameSearchParam(condition)).stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    /**
     * ゲームの検索の検索件数を取得する.
     *
     * @param condition 検索条件
     * @return 件数
     */
    @Override
    public int getSearchCount(final GameSearchCondition condition) {

        return search(condition).size();
    }

    /**
     * ゲームエンティティに変換する.
     *
     * @param dbEntity ゲーブ情報DBエンティティ
     * @return ゲームエンティティ
     */
    private Game convertToEntity(final GameInfo dbEntity) {

        final Game game = new Game(new GameId(dbEntity.getGameId()));
        game.setGenreId(new GenreId(dbEntity.getGameId()));
        game.setCeroRating(CeroRating.fromId(dbEntity.getCeroId()));
        game.setTitle(new Title(dbEntity.getTitle()));
        game.setUrl(new SiteUrl(dbEntity.getSiteUrl()));

        return game;
    }

    /**
     * ゲームの情報をINSERTする.
     *
     * @param game ゲーム
     * @return 新しく発行されたゲームID
     */
    private long insertGame(final Game game) {

        final GameBaseInfo newGame = new GameBaseInfo();

        final long gameId = gameBaseInfoDao.getSequence();

        newGame.setGameId(gameId);
        newGame.setTitle(game.getTitle().getValue());
        newGame.setGenreId(game.getGenreId().getValue());
        newGame.setCeroId(game.getCeroRating().getId());
        newGame.setLockUpdateTimestamp(LocalDateTime.now());
        gameBaseInfoDao.insert(newGame);

        if (!game.getUrl().getValue().isEmpty()) {

            final GameSiteUrl url = new GameSiteUrl();
            url.setGameId(gameId);
            url.setSiteUrl(game.getUrl().getValue());
            gameSiteUrlDao.insert(url);
        }

        return gameId;
    }

    /**
     * ゲームを更新する.
     *
     * @param game ゲーム
     * @return 更新したゲームID
     */
    private long updateGame(final Game game) {

        final GameBaseInfo gameInfo = gameBaseInfoDao.selectEntytById(game.getId().getValue()).get();
        gameInfo.setTitle(game.getTitle().getValue());
        gameInfo.setGenreId(game.getGenreId().getValue());
        gameInfo.setCeroId(game.getCeroRating().getId());
        gameInfo.setLockUpdateTimestamp(LocalDateTime.now());
        gameBaseInfoDao.update(gameInfo);

        saveGameSiteUrl(game.getId(), game.getUrl());

        return game.getId().getValue();
    }

    /**
     * サイトのURLを保存する.
     *
     * @param gameId ゲームID
     * @param siteUrl ゲームサイトURL
     */
    private void saveGameSiteUrl(final GameId gameId, final SiteUrl siteUrl) {

        if (siteUrl.getValue().isEmpty()) {

            gameSiteUrlDao.delete(gameId.getValue());
            return;
        }

        final Optional<GameSiteUrl> optSite = gameSiteUrlDao.selectById(gameId.getValue());

        if (optSite.isPresent()) {

            final GameSiteUrl updateTarget = optSite.get();
            updateTarget.setSiteUrl(siteUrl.getValue());

            gameSiteUrlDao.update(updateTarget);
        } else {

            final GameSiteUrl newSiteUrl = new GameSiteUrl();
            newSiteUrl.setGameId(gameId.getValue());
            newSiteUrl.setSiteUrl(siteUrl.getValue());

            gameSiteUrlDao.insert(newSiteUrl);
        }
    }
}
