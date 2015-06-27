package jp.ne.glory.web.admin.game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;
import jp.ne.glory.domain.game.value.search.GameSearchOrder;
import jp.ne.glory.domain.game.value.search.GameSearchResults;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.game.GameBean;
import jp.ne.glory.ui.admin.game.GameListView;
import jp.ne.glory.ui.admin.game.GameSearchConditionBean;
import jp.ne.glory.web.common.PagePaths;
import jp.ne.glory.web.common.PagerInfo;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ゲーム一覧.
 *
 * @author Junki Yamada
 */
@CertifyTarget
@RequestScoped
@Path("/admin/games")
public class Games {

    /**
     * ゲーム検索.
     */
    private final GameSearch gameSearch;

    /**
     * ジャンル検索.
     */
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    Games() {

        this.gameSearch = null;
        this.genreSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param gameSearch ゲーム検索
     * @param genreSearch ジャンル検索
     */
    @Inject
    public Games(final GameSearch gameSearch, final GenreSearch genreSearch) {

        this.gameSearch = gameSearch;
        this.genreSearch = genreSearch;
    }

    /**
     * 初期表示.
     *
     * @return ゲーム一覧
     */
    @GET
    public Viewable view() {

        final GameSearchCondition condition = createSearchCondition();
        final GameSearchResults results = gameSearch.search(condition);
        final GameListView listView = createView(results, new GameSearchConditionBean());

        return new Viewable(PagePaths.GAME_LIST, listView);
    }

    /**
     * 検索.
     *
     * @param conditionParam 検索条件
     * @return ゲーム一覧
     */
    @GET
    @Path("search")
    public Viewable search(@BeanParam final GameSearchConditionBean conditionParam) {

        final GameSearchCondition condition = createSearchCondition(conditionParam);
        final GameSearchResults results = gameSearch.search(condition);
        final GameListView listView = createView(results, conditionParam);

        return new Viewable(PagePaths.GAME_LIST, listView);
    }

    /**
     * 検索条件オブジェクトを作成する.<br>
     * ページ番号、ソート順のみ設定する.
     *
     * @return 検索条件
     */
    private GameSearchCondition createSearchCondition() {

        final GameSearchCondition condition = new GameSearchCondition();
        condition.setOrder(GameSearchOrder.IdDesc);
        condition.setLotPerCount(20);

        return condition;
    }

    /**
     * 検索条件オブジェクトを作成する.<br>
     * パラメータでの検索値を設定する.
     *
     * @param conditionParam 検索条件パラメータ
     * @return 検索条件
     */
    private GameSearchCondition createSearchCondition(final GameSearchConditionBean conditionParam) {

        final GameSearchCondition condition = createSearchCondition();

        if (conditionParam.getGenreId() != null) {

            condition.setGenreId(new GenreId(conditionParam.getGenreId()));
        }

        if (conditionParam.getCeroRating() != null) {

            condition.setCeroRating(conditionParam.getCeroRating());
        }

        if (conditionParam.getTitle() != null && !conditionParam.getTitle().isEmpty()) {

            condition.setTitle(new Title(conditionParam.getTitle()));
        }

        if (conditionParam.getPageNumber() != null) {

            condition.setLotNumber(conditionParam.getPageNumber());
        }

        return condition;
    }

    /**
     * viewオブジェクトを作成する.
     *
     * @param results 検索結果
     * @param conditionParam 検索条件
     * @return viewオブジェクト
     */
    private GameListView createView(final GameSearchResults results, final GameSearchConditionBean conditionParam) {

        final GameListView listView = new GameListView();
        listView.setCondition(conditionParam);

        final Map<Long, Genre> genreMap = genreSearch.getAllGenres().stream()
                .collect(Collectors.toMap(e -> e.getId().getValue(), e -> e));

        final List<GameBean> games = results.getResults().stream()
                .map(v -> new GameBean(v, genreMap.get(v.getGenreId().getValue())))
                .collect(Collectors.toList());

        listView.setGames(games);

        listView.setPager(new PagerInfo(results));

        return listView;
    }
}
