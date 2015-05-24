package jp.ne.glory.web.admin.game;

import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;
import jp.ne.glory.domain.game.value.search.GameSearchOrder;
import jp.ne.glory.domain.game.value.search.GameSearchResults;
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
    private final GameSearch search;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    Games() {

        this.search = null;
    }

    /**
     * コンストラクタ.
     *
     * @param search ゲーム検索
     */
    @Inject
    public Games(final GameSearch search) {

        this.search = search;
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

        final GameSearchResults results = search.search(condition);

        final GameListView listView = new GameListView();
        listView.setCondition(new GameSearchConditionBean());

        final List<GameBean> games = results.getResults().stream()
                .map(v -> new GameBean(v))
                .collect(Collectors.toList());

        listView.setGames(games);

        return new Viewable(PagePaths.GAME_LIST, listView);
    }
}
