package jp.ne.glory.web.admin.user;

import java.util.List;
import java.util.stream.IntStream;
import jp.ne.glory.application.user.UserSearch;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepositoryStub;
import jp.ne.glory.test.user.search.UserSearchDataGenerator;
import jp.ne.glory.ui.admin.user.UserBean;
import jp.ne.glory.ui.admin.user.UserListView;
import jp.ne.glory.ui.admin.user.UserSearchConditionBean;
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
public class UsersTest {

    public static class viewのテスト {

        private Users sut = null;
        private UserRepositoryStub stub = null;
        private List<User> userList = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            userList = UserSearchDataGenerator.creaeteUsers(10);

            userList.forEach(stub::save);

            sut = new Users(new UserSearch(stub));
        }

        @Test
        public void 検索条件は未入力_リストは全件表示される() {

            final Viewable viewable = sut.view();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_LIST));
            assertThat(viewable.getModel(), is(instanceOf(UserListView.class)));

            final UserListView actualView = (UserListView) viewable.getModel();

            final UserSearchConditionBean actualCondition = actualView.getCondition();
            assertThat(actualCondition.getLoginId(), is(nullValue()));
            assertThat(actualCondition.getUserName(), is(nullValue()));

            final List<UserBean> actualUsers = actualView.getUsers();
            assertThat(actualUsers.size(), is(userList.size()));

            IntStream.range(0, actualUsers.size()).forEach(i -> {
                final UserBean actualUser = actualUsers.get(i);
                final User expectedUser = userList.get(i);

                assertThat(actualUser.getUserId(), is(expectedUser.getUserId().getValue()));
                assertThat(actualUser.getLoginId(), is(expectedUser.getLoginId().getValue()));
                assertThat(actualUser.getUserName(), is(expectedUser.getUserName().getValue()));
            });
        }
    }

    public static class searchのテスト {

        private Users sut = null;
        private UserRepositoryStub stub = null;
        private List<User> userList = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            userList = UserSearchDataGenerator.creaeteUsers(10);

            userList.forEach(stub::save);

            sut = new Users(new UserSearch(stub));
        }

        @Test
        public void 検索条件が入力されていない場合は全件が取得される() {

            final Viewable viewable = sut.search(new UserSearchConditionBean());

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_LIST));
            assertThat(viewable.getModel(), is(instanceOf(UserListView.class)));

            final UserListView actualView = (UserListView) viewable.getModel();

            final UserSearchConditionBean actualCondition = actualView.getCondition();
            assertThat(actualCondition.getLoginId(), is(nullValue()));
            assertThat(actualCondition.getUserName(), is(nullValue()));

            final List<UserBean> actualUsers = actualView.getUsers();
            assertThat(actualUsers.size(), is(userList.size()));

            IntStream.range(0, actualUsers.size()).forEach(i -> {
                final UserBean actualUser = actualUsers.get(i);
                final User expectedUser = userList.get(i);

                assertThat(actualUser.getUserId(), is(expectedUser.getUserId().getValue()));
                assertThat(actualUser.getLoginId(), is(expectedUser.getLoginId().getValue()));
                assertThat(actualUser.getUserName(), is(expectedUser.getUserName().getValue()));
            });
        }

        @Test
        public void 検索条件が入力されている場合は検索条件にマッチした情報が検索される() {

            final UserSearchConditionBean expectedCondition = new UserSearchConditionBean();
            expectedCondition.setLoginId("test-user-5");
            expectedCondition.setUserName("ユーザ5");

            final Viewable viewable = sut.search(expectedCondition);

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_LIST));
            assertThat(viewable.getModel(), is(instanceOf(UserListView.class)));

            final UserListView actualView = (UserListView) viewable.getModel();

            final UserSearchConditionBean actualCondition = actualView.getCondition();
            assertThat(actualCondition.getLoginId(), is(expectedCondition.getLoginId()));
            assertThat(actualCondition.getUserName(), is(expectedCondition.getUserName()));

            final List<UserBean> actualUsers = actualView.getUsers();
            assertThat(actualUsers.size(), is(1));

            final UserBean actualUser = actualUsers.get(0);

            assertThat(actualUser.getLoginId(), is(expectedCondition.getLoginId()));
            assertThat(actualUser.getUserName(), is(expectedCondition.getUserName()));
        }
    }
}