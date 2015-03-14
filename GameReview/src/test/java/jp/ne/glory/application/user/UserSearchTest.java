
package jp.ne.glory.application.user;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepositoryStub;
import jp.ne.glory.domain.user.value.Authorities;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.search.UserSearchCondition;
import jp.ne.glory.domain.user.value.search.UserSearchResults;
import jp.ne.glory.test.user.search.UserSearchDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


@RunWith(Enclosed.class)
public class UserSearchTest {

    public static class getAllのテスト {

        private UserSearch sut = null;
        private UserRepositoryStub stub = null;
        private List<User> userList = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            userList = UserSearchDataGenerator.creaeteUsers(10);

            userList.forEach(stub::save);

            sut = new UserSearch(stub);
        }

        @Test
        public void 全てのユーザが取得できる() {

            final List<User> actualList = sut.getAll();

            assertThat(actualList.size(), is(actualList.size()));

            IntStream.range(0, userList.size()).forEach(v -> {
                final User actualUser = actualList.get(v);
                final User expectedUser = userList.get(v);

                assertThat(actualUser.getUserId().getValue(), is(expectedUser.getUserId().getValue()));
                assertThat(actualUser.getUserName().getValue(), is(expectedUser.getUserName().getValue()));
                assertThat(actualUser.getLoginId().getValue(), is(expectedUser.getLoginId().getValue()));
                assertThat(actualUser.getPassword().getValue(), is(expectedUser.getPassword().getValue()));

                final Authorities actualAuthorities = actualUser.getAuthorities();
                final Authorities expectedAuthorities = expectedUser.getAuthorities();
                Arrays.stream(Authority.values()).forEach(a -> {
                    assertThat(actualAuthorities.hasAuthority(a), is(expectedAuthorities.hasAuthority(a)));
                });
            });
        }
    }

    public static class searchByのテスト {

        private UserSearch sut = null;
        private UserRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            UserSearchDataGenerator.creaeteUsers(10).forEach(stub::save);
            sut = new UserSearch(stub);
        }

        @Test
        public void ログインIDで検索して結果があった場合_Userが返却される() {

            final Optional<User> actualResult = sut.searchBy(new LoginId("test-user-5"));

            assertThat(actualResult.isPresent(), is(true));

            final User actualUser = actualResult.get();

            assertThat(actualUser.getUserId().getValue(), is(5l));
            assertThat(actualUser.getLoginId().getValue(), is("test-user-5"));
        }

        @Test
        public void ログインIDで検索して結果がなかった場合_Userが返却されない() {

            final Optional<User> actualResult = sut.searchBy(new LoginId("test-user-99999"));

            assertThat(actualResult.isPresent(), is(false));
        }
    }

    public static class searchのテスト {

        private UserSearch sut = null;
        private UserRepositoryStub stub = null;
        private List<User> userList = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            userList = UserSearchDataGenerator.creaeteUsers(100);
            userList.forEach(stub::save);
            sut = new UserSearch(stub);
        }

        @Test
        public void 検索条件なしの場合は全件がページングなしで返却される() {

            final UserSearchResults actualResult = sut.search(new UserSearchCondition());

            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(false));
            assertThat(actualResult.getAllCount(), is(100));

            final UserSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getLoginId(), is(nullValue()));
            assertThat(actualCondition.getLotNumber(), is(1));
            assertThat(actualCondition.getLotPerCount(), is(0));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<User> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(userList.size()));
        }

        @Test
        public void 検索条件なし_1ページ5件で1ページ目を表示する場合__前ページなし_次ページありとなる() {

            final UserSearchCondition condition = new UserSearchCondition();
            condition.setLotNumber(1);
            condition.setLotPerCount(5);

            final UserSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(true));
            assertThat(actualResult.hasPrevLot(), is(false));
            assertThat(actualResult.getAllCount(), is(100));

            final UserSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getLoginId(), is(nullValue()));
            assertThat(actualCondition.getLotNumber(), is(1));
            assertThat(actualCondition.getLotPerCount(), is(5));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<User> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(5));
        }

        @Test
        public void 検索条件なし_1ページ5件で10ページ目を表示する場合__前ページあり_次ページありとなる() {

            final UserSearchCondition condition = new UserSearchCondition();
            condition.setLotNumber(10);
            condition.setLotPerCount(5);

            final UserSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(true));
            assertThat(actualResult.hasPrevLot(), is(true));
            assertThat(actualResult.getAllCount(), is(100));

            final UserSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getLoginId(), is(nullValue()));
            assertThat(actualCondition.getLotNumber(), is(10));
            assertThat(actualCondition.getLotPerCount(), is(5));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<User> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(5));
        }

        @Test
        public void 検索条件なし_1ページ5件で20ページ目を表示する場合__前ページあり_次ページなしとなる() {

            final UserSearchCondition condition = new UserSearchCondition();
            condition.setLotNumber(20);
            condition.setLotPerCount(5);

            final UserSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(true));
            assertThat(actualResult.getAllCount(), is(100));

            final UserSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getLoginId(), is(nullValue()));
            assertThat(actualCondition.getLotNumber(), is(20));
            assertThat(actualCondition.getLotPerCount(), is(5));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<User> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(5));
        }

        @Test
        public void ログインIDを指定するとログインIDに一致するユーザが取得できる() {

            final UserSearchCondition condition = new UserSearchCondition();
            final LoginId expectedUserId = userList.get(12).getLoginId();
            condition.setLoginId(expectedUserId);

            final UserSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(false));
            assertThat(actualResult.getAllCount(), is(1));

            final UserSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getLoginId().getValue(), is(expectedUserId.getValue()));
            assertThat(actualCondition.getLotNumber(), is(1));
            assertThat(actualCondition.getLotPerCount(), is(0));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<User> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(1));

            final User actualUser = actualList.get(0);

            assertThat(actualUser.getLoginId().getValue(), is(expectedUserId.getValue()));
        }

    }
}