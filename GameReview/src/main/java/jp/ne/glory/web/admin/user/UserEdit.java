package jp.ne.glory.web.admin.user;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.user.UserRegister;
import jp.ne.glory.application.user.UserRegisterResult;
import jp.ne.glory.application.user.UserSearch;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.infra.encryption.Encryption;
import jp.ne.glory.ui.admin.user.UserEditView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ユーザ編集
 *
 * @author Junki Yamada
 */
@CertifyTarget
@Path("/admin/user/{id}/edit")
@RequestScoped
public class UserEdit {

    /**
     * ユーザ検索.
     */
    private final UserSearch userSearch;

    /**
     * パスワード暗号化.
     */
    private final Encryption encryption;

    /**
     * ユーザ登録.
     */
    private final UserRegister userRegister;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    UserEdit() {
        this.userSearch = null;
        this.encryption = null;
        this.userRegister = null;
    }

    /**
     * コンストラクタ.
     *
     * @param userSearch ユーザ検索
     * @param encryption 暗号化オブジェクト
     * @param userRegister ユーザ登録
     */
    @Inject
    public UserEdit(final UserSearch userSearch, final Encryption encryption, final UserRegister userRegister) {

        this.userSearch = userSearch;
        this.encryption = encryption;
        this.userRegister = userRegister;
    }

    /**
     * ユーザIDをキーにユーザ編集を表示する.
     *
     * @param userId ユーザID
     * @return レスポンス
     */
    @GET
    public Response view(@PathParam("id") final long userId) {

        final Optional<User> result = userSearch.searchBy(new UserId(userId));

        if (!result.isPresent()) {

            return redirectNotFound();
        }

        return buildOkToEdit(result.get());
    }

    /**
     * ユーザ情報を変更する.
     *
     * @param paramUserId ユーザID
     * @param paramUserEdit 変更情報
     * @return レスポンス
     */
    @POST
    public Response completeEdit(@PathParam("id") final long paramUserId, @BeanParam final UserEditView paramUserEdit) {

        final UserId userId = new UserId(paramUserEdit.getUserId());

        if (paramUserEdit.getUserId() == null || paramUserEdit.getUserId() != paramUserId) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final Optional<User> userOpt = userSearch.searchBy(userId);

        if (!userOpt.isPresent()) {

            return redirectNotFound();
        }

        final UserRegisterResult result = finishEdit(userOpt.get(), paramUserEdit);
        final ValidateErrors errors = result.getErrors();

        if (errors.hasError()) {

            return buildOkToReviewEdit(paramUserEdit, errors);
        }

        return redirectView(paramUserId);
    }

    /**
     * ユーザが見つからない画面にリダイレクトする.
     *
     * @return リダイレクトレスポンス
     */
    private Response redirectNotFound() {

        final URI uri = UriBuilder.fromMethod(UserDetail.class, "notFound").build();
        return Response.seeOther(uri).build();
    }

    /**
     * 表示画面にリダイレクトする.
     *
     * @param paramUserId ユーザID
     * @return リダイレクトレスポンス
     */
    private Response redirectView(final long paramUserId) {

        final String urlTemplte = UriBuilder.fromResource(UserDetail.class).toTemplate();
        final URI uri = UriBuilder.fromUri(urlTemplte).resolveTemplate("id", paramUserId).build();

        return Response.seeOther(uri).build();
    }

    /**
     * ユーザの編集を完了する.
     *
     * @param baseUser ベースユーザ
     * @param paramUserEdit ユーザ編集情報
     * @return 登録結果
     */
    private UserRegisterResult finishEdit(final User baseUser, final UserEditView paramUserEdit) {

        final User user = new User(baseUser.getUserId());
        user.setLoginId(new LoginId(paramUserEdit.getLoginId()));
        user.setUserName(new UserName(paramUserEdit.getUserName()));

        final String inputPassword = paramUserEdit.getPassword();
        if (inputPassword == null || inputPassword.isEmpty()) {

            user.setPassword(baseUser.getPassword());
        } else {

            user.setPassword(encryption.encrypt(paramUserEdit.getPassword()));
        }
        
        final Set<Authority> newAuthorites = createAuthorities(paramUserEdit.getAuthorityValues());
        newAuthorites.stream().forEach(v -> user.getAuthorities().add(v));

        return userRegister.finishEdit(user);
    }

    /**
     * 編集画面を表示する.<br>
     * ステータスはOK.
     *
     * @param paramUserEdit ユーザ編集情報
     * @param errors エラー情報
     * @return OKレスポンス
     */
    private Response buildOkToReviewEdit(final UserEditView paramUserEdit, final ValidateErrors errors) {

        paramUserEdit.getErrors().addAll(errors);
        final Viewable viewable = new Viewable(PagePaths.USER_EDIT, paramUserEdit);

        return Response.ok(viewable).build();
    }

    /**
     * 編集画面を表示する.<br>
     * ステータスはOK
     *
     * @param user ユーザ
     * @return OKレスポンス
     */
    private Response buildOkToEdit(final User user) {

        final UserEditView view = new UserEditView(user);
        final Viewable viewable = new Viewable(PagePaths.USER_EDIT, view);

        return Response.ok(viewable).build();
    }

    /**
     * 権限セットを作成する.
     *
     * @param authorityValues 権限入力値
     * @return 権限セット
     */
    private Set<Authority> createAuthorities(final List<String> authorityValues) {

        final Set<Authority> returnSet = new HashSet<>();

        if (authorityValues == null || authorityValues.isEmpty()) {

            returnSet.add(Authority.None);
            return returnSet;
        }

        final Predicate<String> isNumber = (String v) -> !(v == null || v.isEmpty());

        authorityValues.stream()
                .filter(isNumber)
                .map(v -> Authority.getByAuthorityId(Integer.valueOf(v)))
                .forEach(v -> returnSet.add(v));

        return returnSet;
    }
}
