package jp.ne.glory.web.admin.user;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.user.UserRegister;
import jp.ne.glory.application.user.UserRegisterResult;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.infra.encryption.Encryption;
import jp.ne.glory.ui.admin.user.UserEditView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ユーザ作成
 *
 * @author Admin
 */
@CertifyTarget
@Path("/admin/user/create")
public class UserCreate {

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
    UserCreate() {
        this.encryption = null;
        this.userRegister = null;
    }

    /**
     * コンストラクタ.
     *
     * @param encryption 暗号化オブジェクト
     * @param userRegister ユーザ登録
     */
    @Inject
    public UserCreate(final Encryption encryption, final UserRegister userRegister) {

        this.encryption = encryption;
        this.userRegister = userRegister;
    }

    /**
     * 新規作成画面表示処理.
     *
     * @return 新規作成画面
     */
    @GET
    public Viewable view() {

        return new Viewable(PagePaths.USER_CREATE, new UserEditView());
    }

    /**
     * ユーザ作成.
     *
     * @param inputData 入力データ
     * @return 作成成功の場合：ユーザ詳細、失敗した場合：新規作成画面
     */
    @POST
    public Response create(@BeanParam final UserEditView inputData) {

        final User user = convertToEntity(inputData);
        final UserRegisterResult result = userRegister.register(user);
        final ValidateErrors errors = result.getErrors();

        if (errors.hasError()) {

            return buildOkToCreateView(inputData, errors);
        }

        return redirectView(result.getRegisteredUserId().getValue());
    }

    /**
     * ユーザエンティティに変換する.
     *
     * @param inputData 入力データ
     * @return ユーザエンティティ
     */
    private User convertToEntity(final UserEditView inputData) {
        
        final User user = new User();
        
        user.setLoginId(new LoginId(inputData.getLoginId()));
        user.setUserName(new UserName(inputData.getUserName()));

        final Password encryptedPasssword = encryption.encrypt(inputData.getPassword());
        user.setPassword(encryptedPasssword);

        final Set<Authority> newAuthorites = createAuthorities(inputData.getAuthorityValues());
        newAuthorites.stream().forEach(v -> user.getAuthorities().add(v));

        return user;
    }

    /**
     * 編集画面を表示する.<br>
     * ステータスはOK.
     *
     * @param paramUserEdit ユーザ編集情報
     * @param errors エラー情報
     * @return OKレスポンス
     */
    private Response buildOkToCreateView(final UserEditView paramUserEdit, final ValidateErrors errors) {

        paramUserEdit.getErrors().addAll(errors);
        paramUserEdit.setPassword(null);
        final Viewable viewable = new Viewable(PagePaths.USER_CREATE, paramUserEdit);

        return Response.ok(viewable).build();
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
