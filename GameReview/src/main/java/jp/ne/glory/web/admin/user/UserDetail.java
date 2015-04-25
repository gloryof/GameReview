package jp.ne.glory.web.admin.user;

import java.net.URI;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.user.UserSearch;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.user.UserDetailView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ユーザ詳細
 *
 * @author Junki Yamada
 */
@CertifyTarget
@Path("/admin/user/{id}")
@RequestScoped
public class UserDetail {

    /**
     * ユーザ検索.
     */
    private final UserSearch userSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    UserDetail() {

        userSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param userSearch ユーザ検索
     */
    @Inject
    public UserDetail(final UserSearch userSearch) {

        this.userSearch = userSearch;
    }

    /**
     * ユーザIDをキーにユーザ詳細を表示する.
     *
     * @param userId ユーザID
     * @return レスポンス
     */
    @GET
    public Response view(@PathParam("id") final long userId) {

        final Optional<User> result = userSearch.searchBy(new UserId(userId));

        if (!result.isPresent()) {

            return redirectNotFound(userId);
        }

        return buildOkToDetail(result.get());
    }

    /**
     * ユーザが見つからなかった場合の画面を表示する.
     *
     * @return エラー画面
     */
    @Path("notFound")
    @GET
    public Viewable notFound() {

        return new Viewable(PagePaths.USER_NOT_FOUND);
    }

    /**
     * ユーザが見つからない画面にリダイレクトする.
     *
     * @param userId ユーザID
     * @return リダイレクトレスポンス
     */
    private Response redirectNotFound(final long userId) {

        final String base = UriBuilder.fromResource(UserDetail.class).toTemplate();
        final String append = UriBuilder.fromMethod(UserDetail.class, "notFound").toTemplate();

        final UriBuilder builder = UriBuilder.fromUri(base + append);
        builder.resolveTemplate("id", userId);

        final URI uri = builder.build();

        return Response.seeOther(uri).build();
    }
    /**
     * 詳細画面を表示する.<br>
     * ステータスはOK
     *
     * @param user ユーザ
     * @return OKレスポンス
     */
    private Response buildOkToDetail(final User user) {

        final UserDetailView view = new UserDetailView(user);
        final Viewable viewable = new Viewable(PagePaths.USER_DETAIL, view);

        return Response.ok(viewable).build();
    }
}
