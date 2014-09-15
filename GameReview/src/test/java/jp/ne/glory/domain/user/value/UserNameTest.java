package jp.ne.glory.domain.user.value;

import java.util.List;
import jp.ne.glory.test.util.TestUtil;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.user.value.UserName;
import org.junit.Before;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static jp.ne.glory.test.validate.ValidateMatcher.validatedBy;

@RunWith(Enclosed.class)
public class UserNameTest {

    public static class emptyのテスト {

        @Test
        public void ブランクが返却される() {

            final UserName actual = UserName.empty();

            assertThat(actual.value, is(""));
        }
    }

    public static class 正常な値が設定されている場合 {

        private UserName sut = null;

        @Before
        public void setUp() {

            sut = new UserName("テスト");
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が未設定の場合 {

        private UserName sut = null;

        @Before
        public void setUp() {

            sut = new UserName("");
        }

        @Test
        public void validateを行うと必須チェックエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.Required, UserName.LABEL);
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }

    public static class 値が50文字の場合 {

        private UserName sut = null;

        @Before
        public void setUp() {

            sut = new UserName(TestUtil.repeat("a", 50));
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が51文字以上の場合 {

        private UserName sut = null;

        @Before
        public void setUp() {

            sut = new UserName(TestUtil.repeat("a", 51));
        }

        @Test
        public void validateを行うと文字数オーバーエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.MaxLengthOver, UserName.LABEL, "50");
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }
}
