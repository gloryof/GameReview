package jp.ne.glory.domain.user.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.user.value.Authorities;
import jp.ne.groly.domain.user.value.Authority;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static jp.ne.glory.test.validate.ValidateMatcher.validatedBy;

@RunWith(Enclosed.class)
public class AuthoritiesTest {

    public static class コンストラクタで重複ありの権限List設定した場合 {

        private Authorities sut;

        private List<Authority> authorityList;

        @Before
        public void setUp() {

            authorityList = new ArrayList<>();
            authorityList.add(Authority.ReviewPost);
            authorityList.add(Authority.ConfigChange);
            authorityList.add(Authority.ReviewPost);
            authorityList.add(Authority.ConfigChange);

            sut = new Authorities(authorityList);
        }

        @Test
        public void hasAuthorityで設定した権限はすべてtrueが返却される() {

            authorityList.forEach(v -> assertThat(sut.hasAuthority(v), is(true)));
        }

        @Test
        public void hasAuthorityで設定していない権限はfalseが返却される() {

            assertThat(sut.hasAuthority(Authority.None), is(false));
        }

        @Test
        public void hasAuthorityでNullを渡すとfalseが返却される() {

            assertThat(sut.hasAuthority(null), is(false));
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class コンストラクタでパラメータなしの場合 {

        private Authorities sut;

        @Before
        public void setUp() {

            sut = new Authorities();
        }

        @Test
        public void hasAuthorityで設定していない権限はfalseが返却される() {

            assertThat(sut.hasAuthority(Authority.None), is(false));
        }

        @Test
        public void hasAuthorityでNullを渡すとfalseが返却される() {

            assertThat(sut.hasAuthority(null), is(false));
        }

        @Test
        public void addに権限を渡すと権限が追加される() {

            final Authority expected = Authority.ReviewPost;
            sut.add(expected);

            assertThat(sut.hasAuthority(expected), is(true));
        }

        @Test
        public void addにNullを渡すと権限が追加されない() {

            sut.add(null);

            Arrays.stream(Authority.values()).forEach(
                v -> assertThat(sut.hasAuthority(v), is(false))
            );

            assertThat(sut.hasAuthority(null), is(false));
        }

        @Test
        public void validateで必須入力エラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.RequiredSelectOne, Authorities.LABEL);
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }

    public static class 権限が1つ追加されている場合 {

        private Authorities sut;

        @Before
        public void setUp() {

            final List<Authority> authorityList = new ArrayList<>();
            authorityList.add(Authority.ReviewPost);

            sut = new Authorities(authorityList);
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }
}
