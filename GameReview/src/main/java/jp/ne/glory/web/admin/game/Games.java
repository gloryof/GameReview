package jp.ne.glory.web.admin.game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;
import jp.ne.glory.domain.game.value.search.GameSearchOrder;
import jp.ne.glory.domain.game.value.search.GameSearchResults;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.game.GameBean;
import jp.ne.glory.ui.admin.game.GameListView;
import jp.ne.glory.ui.admin.game.GameSearchConditionBean;
import jp.ne.glory.web.common.PagePaths;
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

        final GameSearchCondition condition = new GameSearchCondition();
        condition.setOrder(GameSearchOrder.IdDesc);
        condition.setLotPerCount(20);

        final GameSearchResults results = gameSearch.search(condition);

        final GameListView listView = new GameListView();
        listView.setCondition(new GameSearchConditionBean());

        final Map<Long, Genre> genreMap = genreSearch.getAllGenres().stream()
                .collect(Collectors.toMap(e -> e.getId().getValue(), e -> e));

        final List<GameBean> games = results.getResults().stream()
                .map(v -> new GameBean(v, genreMap.get(v.getGenreId().getValue())))
                .collect(Collectors.toList());

        listView.setGames(games);

        return new Viewable(PagePaths.GAME_LIST, listView);
    }
}
