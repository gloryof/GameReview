package jp.ne.glory.domain.user.validate;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.Authorities;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.test.validate.ValidateErrorsHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class UserValidateRuleTest {

    public static class 全ての値が正常に設定されている場合 {

        private UserValidateRule sut = null;

        @Before
        public void setUp() {

            final User user = new User(new UserId(1L));

            user.getAuthorities().add(Authority.ReviewPost);
            user.setLoginId(new LoginId("test"));
            user.setUserName(new UserName("テストユーザ"));
            user.setPassword(new Password("19CB2A070DDBE8157E17C5DDA0EA38E8AA16FAE1725C1F7AC22747D870368579"));

            sut = new UserValidateRule(user);
        }

        @Test
        public void validateForRegisterを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validateForRegister();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void validateForEditを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validateForEdit();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 全ての項目が未設定の場合 {

        private UserValidateRule sut = null;

        @Before
        public void setUp() {

            sut = new UserValidateRule(new User());
        }

        @Test
        public void validateForRegisterで必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validateForRegister();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.Required, UserName.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, LoginId.LABEL));
            errorList.add(new ValidateError(ErrorInfo.RequiredSelectOne, Authorities.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Password.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

        @Test
        public void validateForEditで全ての必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validateForEdit();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotRegister, User.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, UserName.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, LoginId.LABEL));
            errorList.add(new ValidateError(ErrorInfo.RequiredSelectOne, Authorities.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Password.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }
}
