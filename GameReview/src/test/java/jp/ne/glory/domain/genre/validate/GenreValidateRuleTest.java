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
import static org.junit.Assert.*;

public class GenreValidateRuleTest {

    public static class 全ての値に正常な値が設定されている場合 {

        private GenreValidateRule sut = null;

        @Before
        public void setUp() {

            final Genre genre = new Genre(new GenreId(100L), new GenreName("ジャンル名"));
            sut = new GenreValidateRule(genre);
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

    public static class 全ての値が未設定の場合 {

        private GenreValidateRule sut = null;

        @Before
        public void setUp() {

            final Genre genre = new Genre(GenreId.notNumberingValue(), GenreName.empty());
            sut = new GenreValidateRule(genre);
        }

        @Test
        public void validateForRegisterで必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validateForRegister();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.Required, GenreName.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

        @Test
        public void validateForEditで全ての必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validateForEdit();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotRegister, Genre.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, GenreName.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

    }
}
