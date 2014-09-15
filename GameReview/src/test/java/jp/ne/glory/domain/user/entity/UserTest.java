package jp.ne.glory.domain.user.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.ne.glory.test.validate.ValidateErrorsHelper;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import jp.ne.glory.domain.user.value.Authorities;
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

        @Test
        public void validateを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
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

        @Test
        public void validateで全ての項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.Required, UserName.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, LoginId.LABEL));
            errorList.add(new ValidateError(ErrorInfo.RequiredSelectOne, Authorities.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }
}
