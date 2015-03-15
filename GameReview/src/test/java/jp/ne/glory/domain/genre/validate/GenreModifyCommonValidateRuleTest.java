package jp.ne.glory.domain.genre.validate;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.test.validate.ValidateErrorsHelper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GenreModifyCommonValidateRuleTest {

    public static class 全ての値に正常な値が設定されている場合 {

        private GenreModfyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final Genre genre = new Genre(new GenreId(100L), new GenreName("ジャンル名"));
            sut = new GenreModfyCommonValidateRule(genre);
        }

        @Test
        public void validateを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 全ての値が未設定の場合 {

        private GenreModfyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final Genre genre = new Genre(GenreId.notNumberingValue(), GenreName.empty());
            sut = new GenreModfyCommonValidateRule(genre);
        }

        @Test
        public void validateで必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.Required, GenreName.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }
}
