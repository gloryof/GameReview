
package jp.ne.glory.application.genre;

import jp.ne.glory.application.genre.GenreList;
import java.util.List;
import java.util.stream.IntStream;
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
public class GenreListTest {

    public static class getAllGenresのテスト {

        public GenreList sut = null;
        public GenreRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new GenreRepositoryStub();
            IntStream
                .rangeClosed(1, 10)
                .forEach(i -> {

                    final Genre testData = new Genre(new GenreId(Long.valueOf(i)), new GenreName("ジャンル" + i));
                    stub.save(testData);
                });

            sut = new GenreList(stub);
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

                assertThat(actual.id.isSame(expected.id), is(true));
                assertThat(actual.name.value.equals(expected.name.value), is(true));
            });
        }
    }

}
