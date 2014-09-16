package jp.ne.glory.domain.user.entity;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class UserTest {

    public static class 全ての値が設定されている場合 {

        private User sut;

        private final UserId USER_ID_VALUE = new UserId(123456L);

        @Before
        public void setUp() {

            sut = new User(USER_ID_VALUE);
            Arrays.stream(Authority.values()).forEach(v -> sut.authorities.add(v));
            sut.loginId = new LoginId("test-user");
            sut.userName = new UserName("シュンツ");
        }

        @Test
        public void 設定したユーザIDが設定されている() {

            assertThat(sut.userId.isSame(USER_ID_VALUE), is(true));
        }
        
        @Test
        public void isRegisteredにtrueが設定されている() {

            assertThat(sut.isRegistered(), is(true));
        }
    }

    public static class 全ての値が初期値の場合 {

        private User sut;
        

        @Before
        public void setUp() {

            sut = new User();
        }

        @Test
        public void 全ての値が初期値() {

            assertThat(sut.userId.isSetValue, is(false));
            Arrays.stream(Authority.values())
                .forEach(
                        v -> assertThat(sut.authorities.hasAuthority(v), is(false))
                );
            assertThat(sut.loginId.value, is(LoginId.empty().value));
            assertThat(sut.userName.value, is(UserName.empty().value));
        }
        
        @Test
        public void isRegisteredにfalseが設定されている() {

            assertThat(sut.isRegistered(), is(false));
        }
    }
}
