package jp.ne.glory.web.admin.genre;

import java.net.URI;
import java.util.Optional;
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
import jp.ne.glory.application.genre.GenreRegister;
import jp.ne.glory.application.genre.GenreRegisterResult;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.genre.GenreEditView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ジャンル編集.
 *
 * @author Junki Yamada
 */
@CertifyTarget
@Path("/admin/genre/{genreId}/edit")
@RequestScoped
public class GenreEdit {

    /**
     * ジャンル登録.
     */
    private final GenreRegister register;

    /**
     * ジャンル検索.
     */
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    GenreEdit() {

        this.register = null;
        this.genreSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param register ジャンル登録
     * @param genreSearch ジャンル検索
     */
    @Inject
    public GenreEdit(final GenreRegister register, final GenreSearch genreSearch) {

        this.register = register;
        this.genreSearch = genreSearch;
    }

    /**
     * ジャンルIDをキーにジャンル編集を表示する.
     *
     * @param genreId ジャンルID
     * @return ジャンル編集画面
     */
    @GET
    @Transactional(Transactional.TxType.REQUIRED)
    public Response view(@PathParam("genreId") final long genreId) {

        final Optional<Genre> genreOpt = genreSearch.searchBy(new GenreId(genreId));

        if (!genreOpt.isPresent()) {

            return redirectNotFound(genreId);
        }

        final GenreEditView view = new GenreEditView(genreOpt.get());
        final Viewable viewable = new Viewable(PagePaths.GENRE_EDIT, view);

        return Response.ok(viewable).build();
    }

    /**
     * 編集完了.
     *
     * @param paramGenreId ジャンルID
     * @param inputData 入力データ
     * @return レスポンス
     */
    @POST
    @Transactional(Transactional.TxType.REQUIRED)
    public Response completeEdit(@PathParam("genreId") final long paramGenreId, @BeanParam final GenreEditView inputData) {
        if (inputData.getGenreId() == null || inputData.getGenreId() != paramGenreId) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final GenreId genreId = new GenreId(paramGenreId);
        final Optional<Genre> genreOpt = genreSearch.searchBy(genreId);

        if (!genreOpt.isPresent()) {

            return redirectNotFound(paramGenreId);
        }

        final GenreRegisterResult result = finishEdit(genreOpt.get(), inputData);
        final ValidateErrors errors = result.getErrors();

        if (errors.hasError()) {

            return buildOkToReviewEdit(inputData, errors);
        }

        return redirectView(paramGenreId);
    }

    /**
     * 表示画面にリダイレクトする.
     *
     * @param genreId ジャンルID
     * @return リダイレクトレスポンス
     */
    private Response redirectView(final long genreId) {

        final String urlTemplte = UriBuilder.fromResource(GenreDetail.class).toTemplate();
        final URI uri = UriBuilder.fromUri(urlTemplte).resolveTemplate("genreId", genreId).build();

        return Response.seeOther(uri).build();
    }


    /**
     * ジャンルの編集を完了する.
     *
     * @param baseGenre ベースジャンル
     * @param inputData ジャンル編集情報
     * @return 登録結果
     */
    private GenreRegisterResult finishEdit(final Genre baseGenre, final GenreEditView inputData) {

        final GenreName name = new GenreName(inputData.getName());
        final Genre genre = new Genre(baseGenre.getId(), name);

        return register.finishEdit(genre);
    }

    /**
     * 編集画面を表示する.<br>
     * ステータスはOK.
     *
     * @param inputData ジャンル編集情報
     * @param errors エラー情報
     * @return OKレスポンス
     */
    private Response buildOkToReviewEdit(final GenreEditView inputData, final ValidateErrors errors) {

        inputData.getErrors().addAll(errors);
        final Viewable viewable = new Viewable(PagePaths.GENRE_EDIT, inputData);

        return Response.ok(viewable).build();
    }

    /**
     * ジャンルが見つからない画面にリダイレクトする.
     *
     * @param genreId ジャンルID
     * @return リダイレクトレスポンス
     */
    private Response redirectNotFound(final long genreId) {

        final String base = UriBuilder.fromResource(GenreDetail.class).toTemplate();
        final String append = UriBuilder.fromMethod(GenreDetail.class, "notFound").toTemplate();

        final UriBuilder builder = UriBuilder.fromUri(base + append);
        builder.resolveTemplate("genreId", genreId);

        final URI uri = builder.build();

        return Response.seeOther(uri).build();
    }
}
