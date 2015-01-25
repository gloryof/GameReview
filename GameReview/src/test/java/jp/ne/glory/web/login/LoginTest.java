package jp.ne.glory.web.login;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import jp.ne.glory.application.user.UserAuthenticationStub;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.infra.certify.CertifyControlStub;
import jp.ne.glory.infra.encryption.EncryptionStub;
import jp.ne.glory.test.validate.ValidateErrorsHelper;
import jp.ne.glory.ui.login.LoginView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class LoginTest {

    public static class viewのテスト {

        private Login sut = null;

        private UserAuthenticationStub authStub = null;
        private EncryptionStub encStub = null;
        private CertifyControlStub certifyStub = null;

        @Before
        public void setUp() {

            authStub = new UserAuthenticationStub();
            encStub = new EncryptionStub();
            certifyStub = new CertifyControlStub();

            sut = new Login(authStub, encStub, certifyStub);
        }

        @Test
        public void 値が未設定のLoginViewが返却される() {

            final Viewable viewable = sut.view();

            assertThat(viewable.getTemplateName(), is(PagePaths.LOGIN));

            assertThat(viewable.getModel(), is(instanceOf(LoginView.class)));
            final LoginView actualView = (LoginView) viewable.getModel();

            assertThat(actualView.getLoginId(), is(nullValue()));
            assertThat(actualView.getPassword(), is(nullValue()));
        }
    }

    public static class Loginのテスト {

        private Login sut = null;

        private UserAuthenticationStub authStub = null;
        private EncryptionStub encStub = null;
        private CertifyControlStub certifyStub = null;

        @Before
        public void setUp() {

            authStub = new UserAuthenticationStub();
            encStub = new EncryptionStub();
            certifyStub = new CertifyControlStub();

            sut = new Login(authStub, encStub, certifyStub);
        }

        @Test
        public void 入力チェックエラーがあった場合_ログイン画面に戻る() throws URISyntaxException {

            final LoginView paramView = new LoginView();
            paramView.setLoginId("");
            paramView.setPassword("");

            authStub.certifyComplete = false;
            final Response actualResponse = sut.login(paramView);

            final List<ValidateError> errorList = new ArrayList<>();
            errorList.add(new ValidateError(ErrorInfo.Required, LoginId.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Password.LABEL));

            assertReturnLoginView(paramView, actualResponse, errorList);
        }

        @Test
        public void 認証に失敗した場合_ログイン画面に戻る() throws URISyntaxException {

            final LoginView paramView = new LoginView();
            paramView.setLoginId("user-id-test");
            paramView.setPassword("password-test");

            authStub.certifyComplete = false;
            final Response actualResponse = sut.login(paramView);

            final List<ValidateError> errorList = new ArrayList<>();
            errorList.add(new ValidateError(ErrorInfo.LoginFailed));

            assertReturnLoginView(paramView, actualResponse, errorList);
        }

        @Test
        public void 認証に成功した場合_管理画面にリダイレクトされる() throws URISyntaxException {

            final LoginView paramView = new LoginView();
            paramView.setLoginId("user-id-test");
            paramView.setPassword("password-test");

            authStub.certifyComplete = true;

            final Response actualResponse = sut.login(paramView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(certifyStub.isCertify, is(true));
        }

        private void assertReturnLoginView(final LoginView paramView,
                final Response actualResponse, final List<ValidateError> errorList) {

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.LOGIN));
            assertThat(viewable.getModel(), is(instanceOf(LoginView.class)));

            final LoginView actualView = (LoginView) viewable.getModel();

            assertThat(actualView.getLoginId(), is(paramView.getLoginId()));
            assertThat(actualView.getPassword(), is(nullValue()));
            assertThat(actualView.getErrors().hasError(), is(true));

            final ValidateErrors actualErrors = actualView.getErrors();
            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actualErrors);
            helper.assertErrors(errorList);
        }
    }
}