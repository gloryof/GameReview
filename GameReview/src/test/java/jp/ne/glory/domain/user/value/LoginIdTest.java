package jp.ne.glory.domain.user.value;

import java.util.List;
import jp.ne.glory.test.util.TestUtil;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.user.value.LoginId;
import org.junit.Before;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static jp.ne.glory.test.validate.ValidateMatcher.validatedBy;

@RunWith(Enclosed.class)
public class LoginIdTest  {

    public static class emptyのテスト {

        @Test
        public void ブランクが返却される() {

            final LoginId actual = LoginId.empty();

            assertThat(actual.value, is(""));
        }
    }

    public static class 正常な値が設定されている場合 {

        private LoginId sut = null;

        @Before
        public void setUp() {

            sut = new LoginId("tes-user");
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が未設定の場合 {

        private LoginId sut = null;

        @Before
        public void setUp() {

            sut = new LoginId("");
        }

        @Test
        public void validateを行うと必須チェックエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.Required, LoginId.LABEL);
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }

    public static class 値が50文字の場合 {

        private LoginId sut = null;

        @Before
        public void setUp() {

            sut = new LoginId(TestUtil.repeat("a", 50));
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が51文字以上の場合 {

        private LoginId sut = null;

        @Before
        public void setUp() {

            sut = new LoginId(TestUtil.repeat("a", 51));
        }

        @Test
        public void validateを行うと文字数オーバーエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.MaxLengthOver, LoginId.LABEL, "50");
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }

    public static class 値に使用できない文字のみ設定されている場合 {

        private LoginId sut = null;

        @Before
        public void setUp() {

            sut = new LoginId("あ");
        }

        @Test
        public void validateを行うと入力文字エラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.InvalidCharacters, LoginId.LABEL);
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }
}
