package jp.ne.glory.domain.game.value;

import java.util.List;
import jp.ne.glory.test.util.TestUtil;
import static jp.ne.glory.test.validate.ValidateMatcher.validatedBy;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import jp.ne.groly.domain.game.value.SiteUrl;
import org.junit.Before;
import org.junit.Test;

public class SiteUrlTest {

    public static class emptyメソッドのテスト {
        
        @Test
        public void 空のDateValueオブジェクトが返却される() {

            final SiteUrl actual = SiteUrl.empty();

            assertThat(actual.value, is(""));
        }
    }

    public static class 正常な値が設定されている場合 {

        private SiteUrl sut = null;

        @Before
        public void setUp() {

            sut = new SiteUrl("http://localhost:8080/test/test.html");
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が未設定の場合 {

        private SiteUrl sut = null;

        @Before
        public void setUp() {

            sut = new SiteUrl("");
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が2083文字の場合 {

        private SiteUrl sut = null;

        @Before
        public void setUp() {

            sut = new SiteUrl(TestUtil.repeat("a", 2083));
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 値が2084文字以上の場合 {

        private SiteUrl sut = null;

        @Before
        public void setUp() {

            sut = new SiteUrl(TestUtil.repeat("a", 2084));
        }

        @Test
        public void validateを行うと文字数オーバーエラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.MaxLengthOver, SiteUrl.LABEL, "2,083");
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }

    public static class 値に使用できない文字のみ設定されている場合 {

        private SiteUrl sut = null;

        @Before
        public void setUp() {

            sut = new SiteUrl("あ");
        }

        @Test
        public void validateを行うと入力文字エラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.InvalidCharacters, SiteUrl.LABEL);
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }
}
