package jp.ne.glory.domain.review.value;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import org.junit.Before;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static jp.ne.glory.test.validate.ValidateMatcher.validatedBy;

@RunWith(Enclosed.class)
public class BadPointTest {

    public static class emptyのテスト {

        @Test
        public void ブランクが返却される() {

            final BadPoint actual = BadPoint.empty();

            assertThat(actual.value, is(""));
        }
    }

    public static class 正常な値が設定されている場合 {

        private BadPoint sut = null;

        @Before
        public void setUp() {

            sut = new BadPoint("テストコメント");
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が未設定の場合 {

        private BadPoint sut = null;

        @Before
        public void setUp() {

            sut = new BadPoint("");
        }

        @Test
        public void validateを行うと必須チェックエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.Required, BadPoint.LABEL);
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }
}
