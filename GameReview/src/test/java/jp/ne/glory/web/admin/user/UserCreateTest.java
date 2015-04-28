
package jp.ne.glory.web.admin.user;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.user.UserRegister;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepositoryStub;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.infra.encryption.EncryptionStub;
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
public class UserCreateTest {

    public static class viewのテスト {

        private UserCreate sut = null;
        private UserRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();

            sut = new UserCreate(new EncryptionStub(), new UserRegister(stub));
        }

        @Test
        public void 新規作成画面が表示される() {

            final Viewable actualViewable = sut.view();

            assertThat(actualViewable.getTemplateName(), is(PagePaths.USER_CREATE));
            assertThat(actualViewable.getModel(), is(instanceOf(UserEditView.class)));

            final UserEditView actualView = (UserEditView) actualViewable.getModel();

            assertThat(actualView.getUserId(), is(nullValue()));
            assertThat(actualView.getLoginId(), is(nullValue()));
            assertThat(actualView.getUserName(), is(nullValue()));
            assertThat(actualView.isConfigChangeable(), is(false));
            assertThat(actualView.isReviewPostenable(), is(false));
        }
    }

    public static class createのテスト {

        private UserCreate sut = null;
        private UserRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            sut = new UserCreate(new EncryptionStub(), new UserRegister(stub));
        }

        @Test
        public void 正常に登録できた場合_詳細画面を表示する() {

            final UserEditView paramView = new UserEditView();
            paramView.setLoginId("test-login-id");
            paramView.setPassword("test-password");
            paramView.setUserName("ユーザメイ");

            final List<String> authorites = new ArrayList<>();
            authorites.add(String.valueOf(Authority.ConfigChange.authorityId));
            paramView.setAuthorityValues(authorites);

            final long excepctedUserIdValue = stub.getCurrentSequence();

            final Response actualResponse = sut.create(paramView);

            final URI expectedUri = UriBuilder.fromResource(UserDetail.class).resolveTemplate("id", excepctedUserIdValue).build();
            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(expectedUri));

            final User actualUser = stub.findBy(new UserId(excepctedUserIdValue)).get();
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ConfigChange), is(true));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ReviewPost), is(false));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.None), is(false));
        }

        @Test
        public void レビュー投稿権限のみ保持している場合() {

            final UserEditView expectedView = new UserEditView();
            expectedView.setLoginId("test-login-id");
            expectedView.setPassword("test-password");
            expectedView.setUserName("ユーザメイ");

            final List<String> authorites = new ArrayList<>();
            authorites.add(String.valueOf(Authority.ReviewPost.authorityId));
            expectedView.setAuthorityValues(authorites);

            final long excepctedUserIdValue = stub.getCurrentSequence();

            final Response actualResponse = sut.create(expectedView);

            final User actualUser = stub.findBy(new UserId(excepctedUserIdValue)).get();
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ConfigChange), is(false));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ReviewPost), is(true));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.None), is(false));
        }

        @Test
        public void 何も権限をつけなかった場合() {

            final UserEditView expectedView = new UserEditView();
            expectedView.setLoginId("test-login-id");
            expectedView.setPassword("test-password");
            expectedView.setUserName("ユーザメイ");

            final long excepctedUserIdValue = stub.getCurrentSequence();

            final Response actualResponse = sut.create(expectedView);

            final User actualUser = stub.findBy(new UserId(excepctedUserIdValue)).get();
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ConfigChange), is(false));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.ReviewPost), is(false));
            assertThat(actualUser.getAuthorities().hasAuthority(Authority.None), is(true));
        }

        @Test
        public void 入力エラーがある場合_作成画面を表示する() {

            final User savedUser = new User(new UserId(10L));

            savedUser.getAuthorities().add(Authority.ReviewPost);
            savedUser.setLoginId(new LoginId("test"));
            savedUser.setUserName(new UserName("テストユーザ"));
            savedUser.setPassword(new Password("19CB2A070DDBE8157E17C5DDA0EA38E8AA16FAE1725C1F7AC22747D870368579"));

            stub.save(savedUser);

            final UserEditView expectedView = new UserEditView();
            expectedView.setLoginId(savedUser.getLoginId().getValue());
            expectedView.setPassword("test-password");
            expectedView.setUserName("ユーザメイ");

            final Response actualResponse = sut.create(expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_CREATE));
            assertThat(viewable.getModel(), is(instanceOf(UserEditView.class)));

            final UserEditView actualView = (UserEditView) viewable.getModel();

            assertThat(actualView.getErrors().hasError(), is(true));
            assertThat(actualView.getUserId(), is(nullValue()));
            assertThat(actualView.getLoginId(), is(expectedView.getLoginId()));
            assertThat(actualView.getUserName(), is(expectedView.getUserName()));
            assertThat(actualView.getPassword(), is(nullValue()));
        }
    }
}