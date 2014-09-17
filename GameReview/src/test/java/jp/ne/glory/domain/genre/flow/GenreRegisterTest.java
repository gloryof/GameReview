
package jp.ne.glory.domain.genre.flow;

import java.util.Optional;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GenreRegisterTest {

    public static class registerのテスト {

        private GenreRegister sut = null;

        private GenreRepositoryStub repoStub = null;

        @Before
        public void setUp() {

            repoStub = new GenreRepositoryStub();
            sut = new GenreRegister(repoStub);
        }

        @Test
        public void 正常な値が入力されていれば保存される() {

            final Genre genre = new Genre(GenreId.notNumberingValue(), new GenreName("Newジャンル"));

            final GenreRegisterResult result = sut.register(genre);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(false));

            final Optional<Genre> savedUser = repoStub.findBy(result.registeredGenreId);

            assertThat(savedUser.isPresent(), is(true));
        }

        @Test
        public void 入力に不正がある場合エラーになる() {

            final Genre genre = new Genre(GenreId.notNumberingValue(), GenreName.empty());

            final GenreRegisterResult result = sut.register(genre);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(true));
        }
    }

    public static class finishEditのテスト {

        private GenreRegister sut = null;

        private GenreRepositoryStub repoStub = null;

        @Before
        public void setUp() {

            repoStub = new GenreRepositoryStub();
            sut = new GenreRegister(repoStub);
        }

        @Test
        public void 正常な値が入力されていれば保存される() {

            final Genre genre = new Genre(new GenreId(100L), new GenreName("Newジャンル"));

            final GenreRegisterResult result = sut.finishEdit(genre);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(false));

            final Optional<Genre> savedUser = repoStub.findBy(result.registeredGenreId);

            assertThat(savedUser.isPresent(), is(true));
        }

        @Test
        public void 入力に不正がある場合エラーになる() {

            final Genre genre = new Genre(GenreId.notNumberingValue(), GenreName.empty());

            final GenreRegisterResult result = sut.finishEdit(genre);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(true));
        }

        @Test
        public void IDが設定されていない場合エラーになる() {

            final Genre genre = new Genre(GenreId.notNumberingValue(), new GenreName("Newジャンル"));

            final GenreRegisterResult result = sut.finishEdit(genre);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(true));
        }
    }
}
