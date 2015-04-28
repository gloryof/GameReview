package jp.ne.glory.web.admin.user;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.user.UserRegister;
import jp.ne.glory.application.user.UserSearch;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepositoryStub;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.infra.encryption.EncryptionStub;
import jp.ne.glory.test.user.search.UserSearchDataGenerator;
import jp.ne.glory.ui.admin.user.UserEditView;
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
public class UserEditTest {

    public static class viewのテスト {

        private UserEdit sut = null;
        private UserRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();

            final List<User> userList = UserSearchDataGenerator.creaeteUsers(10);
            userList.forEach(stub::save);

            sut = new UserEdit(new UserSearch(stub), new EncryptionStub(), new UserRegister(stub));
        }

        @Test
        public void 指定したユーザIDに紐付くユーザが取得できる() {

            final Response actualResponse = sut.view(5);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_EDIT));
            assertThat(viewable.getModel(), is(instanceOf(UserEditView.class)));

            final UserEditView actualView = (UserEditView) viewable.getModel();

            assertThat(actualView.getUserId(), is(5L));
            assertThat(actualView.getLoginId(), is("test-user-5"));
            assertThat(actualView.getUserName(), is("ユーザ5"));
            assertThat(actualView.getPassword(), is(nullValue()));
            assertThat(actualView.isConfigChangeable(), is(true));
            assertThat(actualView.isReviewPostenable(), is(false));
        }

        @Test
        public void レビュー権限投稿権限のみを持っている場合() {

            final Response actualResponse = sut.view(6);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_EDIT));
            assertThat(viewable.getModel(), is(instanceOf(UserEditView.class)));

            final UserEditView actualView = (UserEditView) viewable.getModel();

            assertThat(actualView.getUserId(), is(6L));
            assertThat(actualView.isConfigChangeable(), is(false));
            assertThat(actualView.isReviewPostenable(), is(true));
        }

        @Test
        public void 指定したユーザIDが存在しない場合_エラー画面にリダイレクトされる() {

            final Response actualResponse = sut.view(Long.MAX_VALUE);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromMethod(UserDetail.class, "notFound").build()));
        }
    }

    public static class completeEditのテスト {

        private UserEdit sut = null;
        private UserRepositoryStub stub = null;
        private EncryptionStub encrypt = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            encrypt = new EncryptionStub();

            final List<User> userList = UserSearchDataGenerator.creaeteUsers(10);
            userList.forEach(stub::save);

            sut = new UserEdit(new UserSearch(stub), encrypt, new UserRegister(stub));
        }

        @Test
        public void 正常な値が入力されている場合_保存されて_詳細画面を表示する() {

            final long paramUserId = 5L;
            final UserEditView expectedView = new UserEditView();
            expectedView.setUserId(paramUserId);
            expectedView.setLoginId("change-login-id");
            expectedView.setPassword("change-password");
            expectedView.setUserName("変更後ユーザID");

            final List<String> authorites = new ArrayList<>();
            authorites.add(String.valueOf(Authority.ConfigChange.authorityId));
            expectedView.setAuthorityValues(authorites);

            final Response actualResponse = sut.completeEdit(paramUserId, expectedView);
            final String templatePath = UriBuilder.fromResource(UserDetail.class).toTemplate();
            final URI uri = UriBuilder.fromUri(templatePath).resolveTemplate("id", paramUserId).build();

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(uri));

            final User actualUser = stub.findBy(new UserId(paramUserId)).get();
            assertThat(actualUser.getLoginId().getValue(), is(expectedView.getLoginId()));
            assertThat(actualUser.getUserName().getValue(), is(expectedView.getUserName()));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ConfigChange), is(true));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ReviewPost), is(false));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.None), is(false));

            final Password passwordValue = encrypt.encrypt(expectedView.getPassword());
            assertThat(actualUser.getPassword().getValue(), is(passwordValue.getValue()));
        }

        @Test
        public void レビュー投稿権限のみ保持している場合() {

            final long paramUserId = 5L;
            final UserEditView expectedView = new UserEditView();
            expectedView.setUserId(paramUserId);
            expectedView.setLoginId("change-login-id");
            expectedView.setPassword("change-password");
            expectedView.setUserName("変更後ユーザID");

            final List<String> authorites = new ArrayList<>();
            authorites.add(String.valueOf(Authority.ReviewPost.authorityId));
            expectedView.setAuthorityValues(authorites);

            final Response actualResponse = sut.completeEdit(paramUserId, expectedView);

            final User actualUser = stub.findBy(new UserId(paramUserId)).get();
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ConfigChange), is(false));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ReviewPost), is(true));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.None), is(false));
        }

        @Test
        public void 何も権限をつけなかった場合() {

            final long paramUserId = 5L;
            final UserEditView expectedView = new UserEditView();
            expectedView.setUserId(paramUserId);
            expectedView.setLoginId("change-login-id");
            expectedView.setPassword("change-password");
            expectedView.setUserName("変更後ユーザID");
            expectedView.setConfigChangeable(false);
            expectedView.setReviewPostenable(false);

            final Response actualResponse = sut.completeEdit(paramUserId, expectedView);

            final User actualUser = stub.findBy(new UserId(paramUserId)).get();
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ConfigChange), is(expectedView.isConfigChangeable()));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ReviewPost), is(expectedView.isReviewPostenable()));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.None), is(true));
        }

        @Test
        public void パスワードが空文字_他が正常値の場合_パスワードは既存の値が保存される() {

            final long paramUserId = 5L;
            final UserEditView expectedView = new UserEditView();
            expectedView.setUserId(paramUserId);
            expectedView.setLoginId("change-login-id");
            expectedView.setUserName("変更後ユーザID");
            expectedView.setPassword("");

            final User expectedUser = stub.findBy(new UserId(paramUserId)).get();
            final Response actualResponse = sut.completeEdit(paramUserId, expectedView);
            final String templatePath = UriBuilder.fromResource(UserDetail.class).toTemplate();
            final URI uri = UriBuilder.fromUri(templatePath).resolveTemplate("id", paramUserId).build();

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(uri));

            final User actualUser = stub.findBy(new UserId(paramUserId)).get();
            assertThat(actualUser.getLoginId().getValue(), is(expectedView.getLoginId()));
            assertThat(actualUser.getUserName().getValue(), is(expectedView.getUserName()));

            assertThat(actualUser.getPassword().getValue(), is(expectedUser.getPassword().getValue()));
        }

        @Test
        public void パスワードがNull_他が正常値の場合_パスワードは既存の値が保存される() {

            final long paramUserId = 5L;
            final UserEditView expectedView = new UserEditView();
            expectedView.setUserId(paramUserId);
            expectedView.setLoginId("change-login-id");
            expectedView.setUserName("変更後ユーザID");
            expectedView.setPassword(null);

            final User expectedUser = stub.findBy(new UserId(paramUserId)).get();
            final Response actualResponse = sut.completeEdit(paramUserId, expectedView);
            final String templatePath = UriBuilder.fromResource(UserDetail.class).toTemplate();
            final URI uri = UriBuilder.fromUri(templatePath).resolveTemplate("id", paramUserId).build();

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(uri));

            final User actualUser = stub.findBy(new UserId(paramUserId)).get();
            assertThat(actualUser.getLoginId().getValue(), is(expectedView.getLoginId()));
            assertThat(actualUser.getUserName().getValue(), is(expectedView.getUserName()));

            assertThat(actualUser.getPassword().getValue(), is(expectedUser.getPassword().getValue()));
        }

        @Test
        public void ドメイン入力チェックエラーの場合_編集画面を表示する() {

            final long paramUserId = 5L;
            final UserEditView expectedView = new UserEditView();
            expectedView.setUserId(paramUserId);
            expectedView.setLoginId("");
            expectedView.setPassword("change-password");
            expectedView.setUserName("変更後ユーザID");

            final Response actualResponse = sut.completeEdit(paramUserId, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_EDIT));
            assertThat(viewable.getModel(), is(instanceOf(UserEditView.class)));

            final UserEditView actualView = (UserEditView) viewable.getModel();

            assertThat(actualView.getErrors().hasError(), is(true));
            assertThat(actualView.getUserId(), is(expectedView.getUserId()));
            assertThat(actualView.getLoginId(), is(expectedView.getLoginId()));
            assertThat(actualView.getUserName(), is(expectedView.getUserName()));
            assertThat(actualView.getPassword(), is(nullValue()));
        }

        @Test
        public void URLのユーザIDと入力値のユーザIDがとなる場合_Badリクエストエラーになる() {

            final long paramUserId = 5L;
            final UserEditView expectedView = new UserEditView();
            expectedView.setUserId(paramUserId);

            final Response actualResponse = sut.completeEdit(10l, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 入力値のユーザIDが未設定の場合_Badリクエストエラーになる() {

            final long paramUserId = 5L;
            final UserEditView expectedView = new UserEditView();

            final Response actualResponse = sut.completeEdit(paramUserId, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 指定したユーザIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long paramUserId = 100L;
            final UserEditView expectedView = new UserEditView();
            expectedView.setUserId(paramUserId);
            expectedView.setLoginId("");
            expectedView.setPassword("change-password");
            expectedView.setUserName("変更後ユーザID");

            final Response actualResponse = sut.completeEdit(paramUserId, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromMethod(UserDetail.class, "notFound").build()));
        }
    }

}