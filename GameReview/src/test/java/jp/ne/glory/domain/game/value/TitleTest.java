package jp.ne.glory.domain.game.value;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static jp.ne.glory.test.validate.ValidateMatcher.*;

import java.util.List;
import jp.ne.glory.test.util.TestUtil;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;

import jp.ne.groly.domain.game.value.Title;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class TitleTest {

    public static class emptyのテスト {

        @Test
        public void ブランクが返却される() {

            final Title actual = Title.empty();

            assertThat(actual.value, is(""));
        }
    }

    public static class 正常な値が設定されている場合 {

        private Title sut = null;

        @Before
        public void setUp() {

            sut = new Title("テストタイトル");
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が未設定の場合 {

        private Title sut = null;

        @Before
        public void setUp() {

            sut = new Title("");
        }

        @Test
        public void validateを行うと必須チェックエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.Required, Title.LABEL);
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }

    public static class 値が100文字の場合 {

        private Title sut = null;

        @Before
        public void setUp() {

            sut = new Title(TestUtil.repeat("あ", 100));
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が101文字以上の場合 {

        private Title sut = null;

        @Before
        public void setUp() {

            sut = new Title(TestUtil.repeat("あ", 101));
        }

        @Test
        public void validateを行うと文字数オーバーエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.MaxLengthOver, Title.LABEL, "100");
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }
}
