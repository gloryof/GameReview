package jp.ne.glory.web.admin.genre;

import java.net.URI;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.genre.GenreRegister;
import jp.ne.glory.application.genre.GenreRegisterResult;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.genre.GenreEditView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ジャンル作成画面を表示する.
 *
 * @author Junki Yamada
 */
@CertifyTarget
@Path("/admin/genre/create")
public class GenreCreate {

    /**
     * ジャンル登録.
     */
    private GenreRegister register = null;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    GenreCreate() {

        this.register = null;
    }

    /**
     * コンストラクタ.
     *
     * @param register ジャンル登録
     */
    @Inject
    public GenreCreate(final GenreRegister register) {

        this.register = register;
    }


    /**
     * 表示処理.
     *
     * @return ジャンル作成画面
     */
    @GET
    public Viewable view() {

        return new Viewable(PagePaths.GENRE_CREATE, new GenreEditView());
    }

    /**
     * ジャンル作成処理.
     *
     * @param inputData 入力データ
     * @return 作成に成功した場合：詳細画面、入力チェックエラーの場合：ジャンル作成画面
     */
    @POST
    public Response create(@BeanParam final GenreEditView inputData) {

        final Genre genre = convertToEntity(inputData);

        final GenreRegisterResult result = register.register(genre);
        final ValidateErrors errors = result.getErrors();

        if (errors.hasError()) {

            return buildOkToCreateView(inputData, errors);
        }

        return redirectView(result.getRegisteredGenreId().getValue());
    }

    /**
     * ジャンルエンティティに変換する.
     *
     * @param inputData 入力データ
     * @return ジャンル
     */
    private Genre convertToEntity(final GenreEditView inputData) {

        final GenreId notNumbered = GenreId.notNumberingValue();
        final GenreName genreName = new GenreName(inputData.getName());

        return new Genre(notNumbered, genreName);
    }

    /**
     * 編集画面を表示する.<br>
     * ステータスはOK.
     *
     * @param inputData 入力データ
     * @param errors エラー情報
     * @return OKレスポンス
     */
    private Response buildOkToCreateView(final GenreEditView inputData, final ValidateErrors errors) {

        inputData.getErrors().addAll(errors);
        final Viewable viewable = new Viewable(PagePaths.GENRE_CREATE, inputData);

        return Response.ok(viewable).build();
    }

    /**
     * 表示画面にリダイレクトする.
     *
     * @param paramGenreId ジャンルID
     * @return リダイレクトレスポンス
     */
    private Response redirectView(final long paramGenreId) {

        final String urlTemplte = UriBuilder.fromResource(GenreDetail.class).toTemplate();
        final URI uri = UriBuilder.fromUri(urlTemplte).resolveTemplate("genreId", paramGenreId).build();

        return Response.seeOther(uri).build();
    }
}
