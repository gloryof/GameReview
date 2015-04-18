package jp.ne.glory.web.admin.user;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.user.UserRegister;
import jp.ne.glory.application.user.UserSearch;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepositoryStub;
import jp.ne.glory.test.user.search.UserSearchDataGenerator;
import jp.ne.glory.ui.admin.user.UserDetailView;
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
public class UserDetailTest {

    public static class viewのテスト {

        private UserDetail sut = null;
        private UserRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();

            final List<User> userList = UserSearchDataGenerator.creaeteUsers(10);
            userList.forEach(stub::save);

            sut = new UserDetail(new UserSearch(stub), new UserRegister(stub));
        }

        @Test
        public void 指定したユーザIDに紐付くユーザが取得できる() {

            final Response actualResponse = sut.view(5);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_DETAIL));
            assertThat(viewable.getModel(), is(instanceOf(UserDetailView.class)));

            final UserDetailView actualView = (UserDetailView) viewable.getModel();

            assertThat(actualView.getUserId(), is(5L));
            assertThat(actualView.getLoginId(), is("test-user-5"));
            assertThat(actualView.getUserName(), is("ユーザ5"));
            assertThat(actualView.isConfigChangeable(), is(true));
            assertThat(actualView.isReviewPostenable(), is(false));
            assertThat(actualView.isNoneAuthority(), is(false));
        }

        @Test
        public void レビュー権限投稿権限のみを持っている場合() {

            final Response actualResponse = sut.view(6);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_DETAIL));
            assertThat(viewable.getModel(), is(instanceOf(UserDetailView.class)));

            final UserDetailView actualView = (UserDetailView) viewable.getModel();

            assertThat(actualView.getUserId(), is(6L));
            assertThat(actualView.isConfigChangeable(), is(false));
            assertThat(actualView.isReviewPostenable(), is(true));
            assertThat(actualView.isNoneAuthority(), is(false));
        }

        @Test
        public void 権限を保持していない場合() {

            final Response actualResponse = sut.view(4);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_DETAIL));
            assertThat(viewable.getModel(), is(instanceOf(UserDetailView.class)));

            final UserDetailView actualView = (UserDetailView) viewable.getModel();

            assertThat(actualView.getUserId(), is(4L));
            assertThat(actualView.isConfigChangeable(), is(false));
            assertThat(actualView.isReviewPostenable(), is(false));
            assertThat(actualView.isNoneAuthority(), is(true));
        }

        @Test
        public void 指定したユーザIDが存在しない場合_エラー画面にリダイレクトされる() {

            final Response actualResponse = sut.view(Long.MAX_VALUE);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromMethod(UserDetail.class, "notFound").build()));
        }
    }

    public static class notFoundのテスト {

        private UserDetail sut = null;
        private UserRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            sut = new UserDetail(new UserSearch(stub), new UserRegister(stub));
        }

        @Test
        public void エラー画面が表示される() {

            final Viewable viewable = sut.notFound();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_NOT_FOUND));
            assertThat(viewable.getModel(), is(nullValue()));
        }
    }
}