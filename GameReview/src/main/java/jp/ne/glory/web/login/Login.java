package jp.ne.glory.web.login;

import java.net.URI;
import java.net.URISyntaxException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.user.UserAuthentication;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.validate.LoginValidateRule;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.infra.certify.CertifyControl;
import jp.ne.glory.infra.encryption.Encryption;
import jp.ne.glory.ui.login.LoginView;
import jp.ne.glory.web.admin.Top;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ログインリソース.
 *
 * @author Junki Yamada
 */
@RequestScoped
@Path("/login")
public class Login {

    /**
     * ユーザ認証.
     */
    private final UserAuthentication auth;

    /**
     * 暗号化オブジェクト.
     */
    private final Encryption encryption;

    /**
     * 認証コントローラ.
     */
    private final CertifyControl certify;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    protected Login() {

        this.auth = null;
        this.encryption = null;
        this.certify = null;
    }

    /**
     * コンストラクタ.
     *
     * @param auth ユーザ認証
     * @param encryption 暗号化オブジェクト
     * @param certify 認証コントローラ
     */
    @Inject
    public Login(final UserAuthentication auth, final Encryption encryption, final CertifyControl certify) {

        this.auth = auth;
        this.encryption = encryption;
        this.certify = certify;
    }

    /**
     * ログイン画面表示.
     *
     * @return ログイン画面
     */
    @GET
    public Viewable view() {

        return new Viewable(PagePaths.LOGIN, new LoginView());
    }

    /**
     * ログイン処理.
     *
     * @param view ログイン画面View
     * @return ログインに成功した場合：管理画面、ログインに失敗した場合：ログイン画面
     * @throws java.net.URISyntaxException URLの形式が間違っている場合。固定値のため本来起こらない。
     */
    @POST
    public Response login(@BeanParam final LoginView view) throws URISyntaxException {

        final ValidateErrors errors = validateLogin(view);
        if (errors.hasError()) {

            final LoginView loginFailedView = new LoginView();
            loginFailedView.loginId = view.loginId;
            loginFailedView.errors.addAll(errors);

            final Viewable viewable = new Viewable(PagePaths.LOGIN, loginFailedView);

            return Response.ok(viewable).build();
        }

        final LoginId loginId = new LoginId(view.loginId);
        certify.createAuthentication(loginId);

        URI uri = UriBuilder.fromResource(Top.class).build();
        return Response.seeOther(uri).build();
    }

    /**
     * ログイン可能かを検証する.
     *
     * @return 入力チェックエラー
     */
    private ValidateErrors validateLogin(final LoginView view) {

        final ValidateErrors errors = new ValidateErrors();

        final LoginId loginId = new LoginId(view.loginId);
        final Password notEncryptedPassword = new Password(view.password);

        final LoginValidateRule rule = new LoginValidateRule(loginId, notEncryptedPassword);
        errors.addAll(rule.validate());

        if (errors.hasError()) {

            return errors;
        }

        final Password encryptedPassword = encryption.encrypt(view.password);
        if (!auth.isCertify(loginId, encryptedPassword)) {

            errors.add(new ValidateError(ErrorInfo.LoginFailed));
        }

        return errors;
    }
}
