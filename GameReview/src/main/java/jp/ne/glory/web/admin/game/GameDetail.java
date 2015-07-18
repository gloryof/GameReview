package jp.ne.glory.web.admin.game;

import java.net.URI;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.game.GameDetailView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ゲーム詳細.
 *
 * @author Junki Yamada.
 */
@RequestScoped
@Path("/admin/game/{gameId}")
@CertifyTarget
public class GameDetail {

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
    GameDetail() {

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
    public GameDetail(final GameSearch gameSearch, final GenreSearch genreSearch) {

        this.gameSearch = gameSearch;
        this.genreSearch = genreSearch;
    }

    /**
     * 詳細画面を表示する.
     *
     * @param gameId ゲームID
     * @return 詳細画面
     */
    @GET
    public Response view(@PathParam("gameId") final long gameId) {

        final Optional<Game> gameOpt = gameSearch.searchBy(new GameId(gameId));

        if (!gameOpt.isPresent()) {

            return redirectNotFound(gameId);
        }

        final Game game = gameOpt.get();
        final Optional<Genre> genreOpt = genreSearch.searchBy(game.getGenreId());

        final GameDetailView view;
        if (genreOpt.isPresent()) {

            view = new GameDetailView(game, genreOpt.orElse(null));
        } else {

            view = new GameDetailView(game);
        }
        final Viewable viewable = new Viewable(PagePaths.GAME_DETAIL, view);

        return Response.ok(viewable).build();
    }

    /**
     * ジャンルが見つからなかった場合の画面を表示する.
     *
     * @return エラー画面
     */
    @Path("notFound")
    @GET
    public Viewable notFound() {

        return new Viewable(PagePaths.GAME_NOT_FOUND);
    }

    /**
     * ジャンルが見つからない画面にリダイレクトする.
     *
     * @param gameID ゲームID
     * @return リダイレクトレスポンス
     */
    private Response redirectNotFound(final long gameID) {

        final String base = UriBuilder.fromResource(GameDetail.class).toTemplate();
        final String append = UriBuilder.fromMethod(GameDetail.class, "notFound").toTemplate();

        final UriBuilder builder = UriBuilder.fromUri(base + append);
        builder.resolveTemplate("gameId", gameID);

        final URI uri = builder.build();

        return Response.seeOther(uri).build();
    }

}
