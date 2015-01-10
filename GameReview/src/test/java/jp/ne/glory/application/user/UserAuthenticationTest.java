
package jp.ne.glory.application.user;

import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepositoryStub;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserAuthenticationTest {

    public static class authenticateのテスト {

        private UserAuthentication sut = null;

        private UserRepositoryStub repositoryStub = null;

        @Before
        public void setUp() {

            repositoryStub = new UserRepositoryStub();

            final User user1 = new User(new UserId(11L));
            user1.loginId = new LoginId("test");
            user1.password = new Password("password");

            final User user2 = new User(new UserId(12L));
            user2.loginId = new LoginId("hoge");
            user2.password = new Password("fuga");

            repositoryStub.save(user1);
            repositoryStub.save(user2);

            sut = new UserAuthentication(repositoryStub);
        }

        @Test
        public void ログインIDとパスワードが一致している場合_trueが返却される() {

            final boolean actual = sut.isCertify(new LoginId("test"), new Password("password"));
            assertThat(actual, is(true));
        }

        @Test
        public void ログインIDが存在してパスワードが不一致の場合_falseが返却される() {

            final boolean actual = sut.isCertify(new LoginId("test"), new Password("password2"));
            assertThat(actual, is(false));
        }

        @Test
        public void 別アカウントのパスワードと一定していても_falseが返却される() {

            final boolean actual = sut.isCertify(new LoginId("hoge"), new Password("password"));
            assertThat(actual, is(false));
        }

        @Test
        public void 存在しないアカウントの場合_falseが返却される() {

            final boolean actual = sut.isCertify(new LoginId("not-user"), new Password("password"));
            assertThat(actual, is(false));
        }
    }


}