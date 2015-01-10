
package jp.ne.glory.domain.user.validate;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.test.validate.ValidateErrorsHelper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LoginValidateRuleTest {

    public static class 全ての値が正常に設定されている場合 {

        private LoginValidateRule sut = null;

        @Before
        public void setUp() {

            final LoginId loginId = new LoginId("test-user");
            final Password password = new Password("test-password");

            sut = new LoginValidateRule(loginId, password);
        }

        @Test
        public void validateを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 全ての項目が未設定の場合 {

        private LoginValidateRule sut = null;

        @Before
        public void setUp() {

            final LoginId loginId = new LoginId("");
            final Password password = new Password("");

            sut = new LoginValidateRule(loginId, password);
        }

        @Test
        public void validateを実行しても入力チェックエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.Required, LoginId.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Password.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actualErrors);
            helper.assertErrors(errorList);
        }
    }
}