
package jp.ne.glory.application.genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class GenreSearchTest {

    public static class getAllGenresのテスト {

        public GenreSearch sut = null;
        public GenreRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new GenreRepositoryStub();
            LongStream.rangeClosed(1, 10)
                    .mapToObj(i -> new Genre(new GenreId(i), new GenreName("ジャンル" + i)))
                    .forEach(stub::save);

            sut = new GenreSearch(stub);
        }

        @Test
        public void 全てのジャンルが取得される() {

            final List<Genre> exepectedList = stub.getAllGenreList();
            final List<Genre> actualList = sut.getAllGenres();
            final int actualSize = actualList.size();

            assertThat(actualSize, is(exepectedList.size()));

            IntStream.range(0, actualSize).forEach(i -> {

                final Genre actual = actualList.get(i);
                final Genre expected = exepectedList.get(i);

                assertThat(actual.getId().isSame(expected.getId()), is(true));
                assertThat(actual.getName().getValue().equals(expected.getName().getValue()), is(true));
            });
        }
    }

    public static class searchByのテスト {

        public GenreSearch sut = null;
        public GenreRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new GenreRepositoryStub();
            LongStream.rangeClosed(1, 10)
                    .mapToObj(i -> new Genre(new GenreId(i), new GenreName("ジャンル" + i)))
                    .forEach(stub::save);

            sut = new GenreSearch(stub);
        }

        @Test
        public void 該当するジャンルIDがある場合はジャンルが取得できる() {

            final GenreId expectedGenreId = new GenreId(4L);

            final Optional<Genre> actualGenreOpt = sut.searchBy(expectedGenreId);

            assertThat(actualGenreOpt.isPresent(), is(true));

            final Genre actualGenre = actualGenreOpt.get();

            assertThat(actualGenre.getId().getValue(), is(expectedGenreId.getValue()));
            assertThat(actualGenre.getName().getValue(), is("ジャンル4"));
        }

        @Test
        public void 該当するジャンルIDがない場合_Optionalの中身は空() {

            final GenreId expectedGenreId = new GenreId(Long.MAX_VALUE);

            final Optional<Genre> actualGenreOpt = sut.searchBy(expectedGenreId);

            assertThat(actualGenreOpt.isPresent(), is(false));
        }
    }
}
