package jp.ne.glory.domain.user.flow;

import java.util.Arrays;
import java.util.Optional;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepositoryStub;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class UserRegisterTest {

    public static class registerのテスト {

        private UserRegister sut = null;

        private UserRepositoryStub repoStub = null;

        @Before
        public void setUp() {

            repoStub = new UserRepositoryStub();
            sut = new UserRegister(repoStub);
        }

        @Test
        public void 正常な値が入力されていれば保存される() {

            final User user = new User();
            Arrays.stream(Authority.values()).forEach(v -> user.authorities.add(v));
            user.loginId = new LoginId("test-user");
            user.userName = new UserName("シュンツ");

            final UserRegisterResult result = sut.register(user);

            final ValidateErrors errors = result.UserRegisterResult.this.errors;
            assertThat(errors.hasError(), is(false));

            final Optional<User> savedUser = repoStub.findBy(result.registeredUserId);

            assertThat(savedUser.isPresent(), is(true));
        }

        @Test
        public void 入力に不正がある場合エラーになる() {

            final User user = new User();

            final UserRegisterResult result = sut.register(user);

            final ValidateErrors errors = result.UserRegisterResult.this.errors;
            assertThat(errors.hasError(), is(true));
        }
    }

    public static class finishEditのテスト {

        private UserRegister sut = null;

        private UserRepositoryStub repoStub = null;

        @Before
        public void setUp() {

            repoStub = new UserRepositoryStub();
            sut = new UserRegister(repoStub);
        }

        @Test
        public void 正常な値が入力されていれば保存される() {

            final User user = new User(new UserId(100L));
            Arrays.stream(Authority.values()).forEach(v -> user.authorities.add(v));
            user.loginId = new LoginId("test-user");
            user.userName = new UserName("シュンツ");

            final UserRegisterResult result = sut.finishEdit(user);

            final ValidateErrors errors = result.UserRegisterResult.this.errors;
            assertThat(errors.hasError(), is(false));

            final Optional<User> savedUser = repoStub.findBy(result.registeredUserId);

            assertThat(savedUser.isPresent(), is(true));
        }

        @Test
        public void 入力に不正がある場合エラーになる() {

            final User user = new User();

            final UserRegisterResult result = sut.finishEdit(user);

            final ValidateErrors errors = result.UserRegisterResult.this.errors;
            assertThat(errors.hasError(), is(true));
        }

        @Test
        public void IDが設定されていない場合エラーになる() {

            final User user = new User();
            Arrays.stream(Authority.values()).forEach(v -> user.authorities.add(v));
            user.loginId = new LoginId("test-user");
            user.userName = new UserName("シュンツ");

            final UserRegisterResult result = sut.finishEdit(user);

            final ValidateErrors errors = result.UserRegisterResult.this.errors;
            assertThat(errors.hasError(), is(true));
        }
    }
}
