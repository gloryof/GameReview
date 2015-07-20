package jp.ne.glory.web.admin.game;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.game.GameRegister;
import jp.ne.glory.application.game.GameRegisterResult;
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
 * ゲーム登録.
 *
 * @author Junki Yamada
 */
@RequestScoped
@Path("/admin/game/create")
@CertifyTarget
public class GameCreate {

    /**
     * ゲーム登録サービス.
     */
    private final GameRegister register;

    /**
     * ジャンル検索サービス.
     */
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    protected GameCreate() {

        this.register = null;
        this.genreSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param register ゲーム登録サービス
     * @param genreSearch ジャンル検索サービス
     */
    @Inject
    public GameCreate(final GameRegister register, final GenreSearch genreSearch) {

        this.register = register;
        this.genreSearch = genreSearch;
    }

    /**
     * 新規登録画面を表示する.
     *
     * @return 新規登録画面
     */
    @GET
    public Viewable view() {

        final GameEditView viewData = new GameEditView();
        viewData.setGenres(createGenres());

        return new Viewable(PagePaths.GAME_CREATE, viewData);
    }

    /**
     * 新規登録を完了する.
     *
     * @param inputData 入力データ
     * @return 作成に成功した場合：詳細画面、入力チェックエラーの場合：ゲーム作成画面
     */
    @POST
    public Response create(@BeanParam final GameEditView inputData) {

        final Game game = convertToEntity(inputData);

        final GameRegisterResult result = register.register(game);
        final ValidateErrors errors = result.getErrors();

        if (errors.hasError()) {

            return buildOkToCreateView(inputData, errors);
        }

        return redirectView(result.getRegisteredGameId().getValue());
    }

    /**
     * 入力画面を表示する.<br>
     * ステータスはOK.
     *
     * @param inputData 入力データ
     * @param errors エラー情報
     * @return OKレスポンス
     */
    private Response buildOkToCreateView(final GameEditView inputData, final ValidateErrors errors) {

        inputData.getErrors().addAll(errors);
        inputData.setGenres(createGenres());

        final Viewable viewable = new Viewable(PagePaths.GAME_CREATE, inputData);

        return Response.ok(viewable).build();
    }

    /**
     * ゲームエンティティに変換する.
     *
     * @param inputData 入力データ
     * @return ゲーム
     */
    private Game convertToEntity(final GameEditView inputData) {

        final GameId notNumbered = GameId.notNumberingValue();

        final Game game = new Game(notNumbered);
        game.setTitle(new Title(inputData.getTitle()));
        game.setCeroRating(inputData.getCeroRating());
        game.setGenreId(new GenreId(inputData.getGenreId()));
        game.setUrl(new SiteUrl(inputData.getUrl()));

        return game;
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
     * ジャンルリストを作成する.
     *
     * @return ジャンルリスト
     */
    private List<GenreBean> createGenres() {

        return genreSearch.getAllGenres().stream()
                .map(v -> new GenreBean(v))
                .collect(Collectors.toList());
    }
}
