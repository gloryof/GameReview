package jp.ne.glory.web.admin.game;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.game.GameRegister;
import jp.ne.glory.application.game.GameRegisterResult;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.game.GameEditView;
import jp.ne.glory.ui.genre.GenreBean;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ゲーム編集.
 *
 * @author Junki Yamada
 */
@CertifyTarget
@RequestScoped
@Path("/admin/game/{gameId}/edit")
public class GameEdit {

    /**
     * ゲーム登録サービス.
     */
    private final GameRegister register;

    /**
     * ゲーム検索サービス.
     */
    private final GameSearch gameSearch;

    /**
     * ジャンル検索サービス.
     */
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    protected GameEdit() {

        this.register = null;
        this.gameSearch = null;
        this.genreSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param register ゲーム登録サービス
     * @param gameSearch ゲーム検索サービス
     * @param genreSearch ジャンル検索サービス
     */
    @Inject
    public GameEdit(final GameRegister register, final GameSearch gameSearch, final GenreSearch genreSearch) {

        this.register = register;
        this.gameSearch = gameSearch;
        this.genreSearch = genreSearch;
    }

    /**
     * 編集画面を表示する.
     *
     * @param paramGameId ゲームID
     * @return レスポンス
     */
    @GET
    @Transactional(Transactional.TxType.REQUIRED)
    public Response view(@PathParam("gameId") final long paramGameId) {

        final Optional<Game> gameOpt = gameSearch.searchBy(new GameId(paramGameId));

        if (!gameOpt.isPresent()) {

            return redirectNotFound(paramGameId);
        }

        final GameEditView view = new GameEditView(gameOpt.get());
        view.setGenres(createGenres());

        final Viewable viewable = new Viewable(PagePaths.GAME_EDIT, view);

        return Response.ok(viewable).build();
    }

    /**
     * ゲームの編集を完了する.
     *
     * @param paramGameId ゲームID
     * @param inputData 入力データ
     * @return 登録に成功した場合：詳細画面、入力エラーの場合：編集画面
     */
    @POST
    @Transactional(Transactional.TxType.REQUIRED)
    public Response completeEdit(@PathParam("gameId") final long paramGameId, @BeanParam final GameEditView inputData) {

        if (inputData.getGameId() == null || inputData.getGameId() != paramGameId) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final GameId gameId = new GameId(paramGameId);
        final Optional<Game> gameOpt = gameSearch.searchBy(gameId);
        if (!gameOpt.isPresent()) {

            return redirectNotFound(paramGameId);
        }

        final GameRegisterResult result = finishEdit(gameOpt.get(), inputData);
        final ValidateErrors errors = result.getErrors();

        if (errors.hasError()) {

            return buildOkToEditView(inputData, errors);
        }

        return redirectView(result.getRegisteredGameId().getValue());
    }

    /**
     * ジャンルが見つからない画面にリダイレクトする.
     *
     * @param gameId ジャンルID
     * @return リダイレクトレスポンス
     */
    private Response redirectNotFound(final long gameId) {

        final String base = UriBuilder.fromResource(GameDetail.class).toTemplate();
        final String append = UriBuilder.fromMethod(GameDetail.class, "notFound").toTemplate();

        final UriBuilder builder = UriBuilder.fromUri(base + append);
        builder.resolveTemplate("gameId", gameId);

        final URI uri = builder.build();

        return Response.seeOther(uri).build();
    }

    /**
     * 表示画面にリダイレクトする.
     *
     * @param paramGameId ゲームID
     * @return リダイレクトレスポンス
     */
    private Response redirectView(final long paramGameId) {

        final String urlTemplte = UriBuilder.fromResource(GameDetail.class).toTemplate();
        final URI uri = UriBuilder.fromUri(urlTemplte).resolveTemplate("gameId", paramGameId).build();

        return Response.seeOther(uri).build();
    }

    /**
     * 編集画面を表示する.<br>
     * ステータスはOK.
     *
     * @param inputData 入力データ
     * @param errors エラー情報
     * @return OKレスポンス
     */
    private Response buildOkToEditView(final GameEditView inputData, final ValidateErrors errors) {

        inputData.getErrors().addAll(errors);
        inputData.setGenres(createGenres());

        final Viewable viewable = new Viewable(PagePaths.GAME_EDIT, inputData);

        return Response.ok(viewable).build();
    }

    /**
     * ジャンルリストを作成する.
     *
     * @return ジャンルリスト
     */
    private List<GenreBean> createGenres() {

        return genreSearch.getAllGenres().stream()
                .map(v -> new GenreBean(v))
                .collect(Collectors.toList());
    }

    /**
     * ジャンルの編集を完了する.
     *
     * @param game ベースゲーム
     * @param inputData ジャンル編集情報
     * @return 登録結果
     */
    private GameRegisterResult finishEdit(final Game game, final GameEditView inputData) {

        final Game updatedGame = new Game(game.getId());
        updatedGame.setTitle(new Title(inputData.getTitle()));
        updatedGame.setCeroRating(inputData.getCeroRating());
        updatedGame.setGenreId(new GenreId(inputData.getGenreId()));
        updatedGame.setUrl(new SiteUrl(inputData.getUrl()));

        return register.finishEdit(updatedGame);
    }

}
