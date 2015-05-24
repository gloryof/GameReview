
package jp.ne.glory.web.admin.genre;

import java.util.List;
import java.util.stream.IntStream;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.test.genre.list.GenreListDataGenerator;
import jp.ne.glory.ui.admin.genre.GenreListView;
import jp.ne.glory.ui.admin.genre.GenreSearchConditionBean;
import jp.ne.glory.ui.genre.GenreBean;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class GenresTest {

    public static class viewのテスト {

        private Genres sut = null;
        private GenreRepositoryStub stub = null;
        private List<Genre> genreList = null;

        @Before
        public void setUp() {

            stub = new GenreRepositoryStub();
            genreList = GenreListDataGenerator.createGenreList(10);
            genreList.forEach(stub::save);

            sut = new Genres(new GenreSearch(stub));
        }

        @Test
        public void 検索条件は未入力_リストは全件表示される() {

            final Viewable viewable = sut.view();

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_LIST));
            assertThat(viewable.getModel(), is(instanceOf(GenreListView.class)));

            final GenreListView actualView = (GenreListView) viewable.getModel();

            final GenreSearchConditionBean actualCondition = actualView.getCondition();
            assertThat(actualCondition.getGenreName(), is(nullValue()));

            final List<GenreBean> actualGenres = actualView.getGenres();
            assertThat(actualGenres.size(), is(genreList.size()));

            IntStream.range(0, actualGenres.size()).forEach(i -> {
                final GenreBean actualGenre = actualGenres.get(i);
                final Genre expectedGenre = genreList.get(i);

                assertThat(actualGenre.getId(), is(expectedGenre.getId().getValue()));
                assertThat(actualGenre.getName(), is(expectedGenre.getName().getValue()));
            });
        }

    }

    public static class searchのテスト {

        private Genres sut = null;
        private GenreRepositoryStub stub = null;
        private List<Genre> genreList = null;

        @Before
        public void setUp() {

            stub = new GenreRepositoryStub();
            genreList = GenreListDataGenerator.createGenreList(10);
            genreList.forEach(stub::save);

            sut = new Genres(new GenreSearch(stub));
        }

        @Test
        public void 検索条件が入力されていない場合は全件が取得される() {

            final GenreSearchConditionBean condition = new GenreSearchConditionBean();
            final Viewable viewable = sut.search(condition);

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_LIST));
            assertThat(viewable.getModel(), is(instanceOf(GenreListView.class)));

            final GenreListView actualView = (GenreListView) viewable.getModel();

            final GenreSearchConditionBean actualCondition = actualView.getCondition();
            assertThat(actualCondition.getGenreName(), is(nullValue()));

            final List<GenreBean> actualGenres = actualView.getGenres();
            assertThat(actualGenres.size(), is(genreList.size()));

            IntStream.range(0, actualGenres.size()).forEach(i -> {
                final GenreBean actualGenre = actualGenres.get(i);
                final Genre expectedGenre = genreList.get(i);

                assertThat(actualGenre.getId(), is(expectedGenre.getId().getValue()));
                assertThat(actualGenre.getName(), is(expectedGenre.getName().getValue()));
            });
        }

        @Test
        public void 検索条件が入力されている場合は検索条件にマッチした情報が検索される() {

            final GenreSearchConditionBean expectedCondition = new GenreSearchConditionBean();
            expectedCondition.setGenreName("ジャンル5");
            final Viewable viewable = sut.search(expectedCondition);

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_LIST));
            assertThat(viewable.getModel(), is(instanceOf(GenreListView.class)));

            final GenreListView actualView = (GenreListView) viewable.getModel();

            final GenreSearchConditionBean actualCondition = actualView.getCondition();
            assertThat(actualCondition.getGenreName(), is(expectedCondition.getGenreName()));

            final List<GenreBean> actualGenres = actualView.getGenres();
            assertThat(actualGenres.size(), is(1));

            final GenreBean actualGenre = actualGenres.get(0);

            assertThat(actualGenre.getId(), is(5l));
            assertThat(actualGenre.getName(), is(expectedCondition.getGenreName()));
        }
    }
}