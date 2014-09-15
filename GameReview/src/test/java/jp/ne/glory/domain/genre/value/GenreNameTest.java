package jp.ne.glory.domain.genre.value;

import java.util.List;
import jp.ne.glory.test.util.TestUtil;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.game.value.Title;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import jp.ne.groly.domain.genre.value.GenreName;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static jp.ne.glory.test.validate.ValidateMatcher.validatedBy;

@RunWith(Enclosed.class)
public class GenreNameTest {

    public static class emptyのテスト {

        @Test
        public void ブランクが返却される() {

            final GenreName actual = GenreName.empty();

            assertThat(actual.value, is(""));
        }
    }

    public static class 正常な値が設定されている場合 {

        private GenreName sut = null;

        @Before
        public void setUp() {

            sut = new GenreName("ジャンルタイトル");
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が未設定の場合 {

        private GenreName sut = null;

        @Before
        public void setUp() {

            sut = new GenreName("");
        }

        @Test
        public void validateを行うと必須チェックエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.Required, GenreName.LABEL);
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }

    public static class 値が50文字の場合 {

        private GenreName sut = null;

        @Before
        public void setUp() {

            sut = new GenreName(TestUtil.repeat("あ", 50));
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が51文字以上の場合 {

        private GenreName sut = null;

        @Before
        public void setUp() {

            sut = new GenreName(TestUtil.repeat("あ", 51));
        }

        @Test
        public void validateを行うと文字数オーバーエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.MaxLengthOver, GenreName.LABEL, "50");
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }
}
