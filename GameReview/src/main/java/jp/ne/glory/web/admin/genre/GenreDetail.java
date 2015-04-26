package jp.ne.glory.web.admin.genre;

import java.net.URI;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.genre.GenreDetailView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ジャンル詳細.
 *
 * @author Junki Yamada
 */
@Path("/admin/genre/{genreId}")
@CertifyTarget
@RequestScoped
public class GenreDetail {

    /**
     * ジャンルリスト.
     */
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    GenreDetail() {

        genreSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param genreList ジャンルリスト
     */
    @Inject
    public GenreDetail(final GenreSearch genreList) {

        this.genreSearch = genreList;
    }

    /**
     * 詳細表示.
     *
     * @param paramGenreId ジャンルID
     * @return 詳細画面
     */
    @GET
    public Response view(@PathParam("genreId") final long paramGenreId) {

        final Optional<Genre> genreOpt = genreSearch.searchBy(new GenreId(paramGenreId));

        if (!genreOpt.isPresent()) {

            return redirectNotFound(paramGenreId);
        }

        final GenreDetailView view = new GenreDetailView(genreOpt.get());
        final Viewable viewable = new Viewable(PagePaths.GENRE_DETAIL, view);

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

        return new Viewable(PagePaths.GENRE_NOT_FOUND);
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
