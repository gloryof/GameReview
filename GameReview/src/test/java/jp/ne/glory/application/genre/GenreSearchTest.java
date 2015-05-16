
package jp.ne.glory.application.genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.genre.value.search.GenreSearchCondition;
import jp.ne.glory.domain.genre.value.search.GenreSearchResults;
import jp.ne.glory.test.genre.list.GenreListDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.nullValue;
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

    public static class searchのテスト {

        public GenreSearch sut = null;
        public GenreRepositoryStub stub = null;
        private List<Genre> genreList = null;

        @Before
        public void setUp() {

            stub = new GenreRepositoryStub();
            genreList = GenreListDataGenerator.createGenreList(100);
            genreList.forEach(stub::save);
            sut = new GenreSearch(stub);
        }

        @Test
        public void 検索条件なしの場合は全件がページングなしで返却される() {

            final GenreSearchResults actualResult = sut.search(new GenreSearchCondition());

            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(false));
            assertThat(actualResult.getAllCount(), is(100));

            final GenreSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getName(), is(nullValue()));
            assertThat(actualCondition.getLotNumber(), is(1));
            assertThat(actualCondition.getLotPerCount(), is(0));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Genre> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(genreList.size()));
        }

        @Test
        public void 検索条件なし_1ページ5件で1ページ目を表示する場合__前ページなし_次ページありとなる() {

            final GenreSearchCondition condition = new GenreSearchCondition();
            condition.setLotNumber(1);
            condition.setLotPerCount(5);

            final GenreSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(true));
            assertThat(actualResult.hasPrevLot(), is(false));
            assertThat(actualResult.getAllCount(), is(100));

            final GenreSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getName(), is(nullValue()));
            assertThat(actualCondition.getLotNumber(), is(1));
            assertThat(actualCondition.getLotPerCount(), is(5));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Genre> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(5));
        }

        @Test
        public void 検索条件なし_1ページ5件で10ページ目を表示する場合__前ページあり_次ページありとなる() {

            final GenreSearchCondition condition = new GenreSearchCondition();
            condition.setLotNumber(10);
            condition.setLotPerCount(5);

            final GenreSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(true));
            assertThat(actualResult.hasPrevLot(), is(true));
            assertThat(actualResult.getAllCount(), is(100));

            final GenreSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getName(), is(nullValue()));
            assertThat(actualCondition.getLotNumber(), is(10));
            assertThat(actualCondition.getLotPerCount(), is(5));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Genre> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(5));
        }

        @Test
        public void 検索条件なし_1ページ5件で20ページ目を表示する場合__前ページあり_次ページなしとなる() {

            final GenreSearchCondition condition = new GenreSearchCondition();
            condition.setLotNumber(20);
            condition.setLotPerCount(5);

            final GenreSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(true));
            assertThat(actualResult.getAllCount(), is(100));

            final GenreSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getName(), is(nullValue()));
            assertThat(actualCondition.getLotNumber(), is(20));
            assertThat(actualCondition.getLotPerCount(), is(5));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Genre> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(5));
        }

        @Test
        public void ジャンル名を指定するとジャンル名に一致するユーザが取得できる() {

            final GenreSearchCondition condition = new GenreSearchCondition();
            final GenreName expectedGenreName = genreList.get(12).getName();
            condition.setName(expectedGenreName);

            final GenreSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(false));
            assertThat(actualResult.getAllCount(), is(1));

            final GenreSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getName().getValue(), is(expectedGenreName.getValue()));
            assertThat(actualCondition.getLotNumber(), is(1));
            assertThat(actualCondition.getLotPerCount(), is(0));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Genre> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(1));

            final Genre actualGenre = actualList.get(0);

            assertThat(actualGenre.getName().getValue(), is(expectedGenreName.getValue()));
        }

    }
}
