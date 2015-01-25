package jp.ne.glory.domain.user.entity;

import java.util.Arrays;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class UserTest {

    public static class 全ての値が設定されている場合 {

        private User sut;

        private final UserId USER_ID_VALUE = new UserId(123456L);

        @Before
        public void setUp() {

            sut = new User(USER_ID_VALUE);
            Arrays.stream(Authority.values()).forEach(v -> sut.getAuthorities().add(v));
            sut.setLoginId(new LoginId("test-user"));
            sut.setUserName(new UserName("シュンツ"));
            sut.setPassword(new Password("19CB2A070DDBE8157E17C5DDA0EA38E8AA16FAE1725C1F7AC22747D870368579"));
        }

        @Test
        public void 設定したユーザIDが設定されている() {

            assertThat(sut.getUserId().isSame(USER_ID_VALUE), is(true));
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

            assertThat(sut.getUserId().isSetValue(), is(false));
            Arrays.stream(Authority.values())
                .forEach(
                            v -> assertThat(sut.getAuthorities().hasAuthority(v), is(false))
                    );
            assertThat(sut.getLoginId().getValue(), is(LoginId.empty().getValue()));
            assertThat(sut.getUserName().getValue(), is(UserName.empty().getValue()));
        }
        
        @Test
        public void isRegisteredにfalseが設定されている() {

            assertThat(sut.isRegistered(), is(false));
        }
    }
}
