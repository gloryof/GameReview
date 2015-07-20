
package jp.ne.glory.web.admin.game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.test.game.search.GameSearchDataGenerator;
import jp.ne.glory.test.genre.list.GenreListDataGenerator;
import jp.ne.glory.ui.admin.game.GameBean;
import jp.ne.glory.ui.admin.game.GameListView;
import jp.ne.glory.ui.admin.game.GameSearchConditionBean;
import jp.ne.glory.ui.genre.GenreBean;
import jp.ne.glory.web.common.PagePaths;
import jp.ne.glory.web.common.PagerInfo;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class GamesTest {

    private static class TestTool {

        public static void assertRatings(final List<CeroRating> actualCeroRatings) {

            final CeroRating[] expcetedRatings = CeroRating.values();
            final int exptectedLength = expcetedRatings.length - 1;

            assertThat(actualCeroRatings.size(), is(exptectedLength));
            for (int i = 0; i < exptectedLength; i++) {

                final CeroRating expectedRating = expcetedRatings[i];
                final CeroRating actualRating = actualCeroRatings.get(i);

                assertThat(actualRating, is(expectedRating));
            }
        }

        public static void assertGenres(final List<GenreBean> actualGenres, final List<Genre> expectedGenres) {

            assertThat(actualGenres.size(), is(expectedGenres.size()));
            IntStream.range(0, actualGenres.size()).forEach(i -> {
                final GenreBean actualGenre = actualGenres.get(i);
                final Genre expectedGenre = expectedGenres.get(i);

                assertThat(actualGenre.getId(), is(expectedGenre.getId().getValue()));
                assertThat(actualGenre.getName(), is(expectedGenre.getName().getValue()));
            });
        }
    }

    public static class viewのテスト {

        private Games sut = null;
        private GameRepositoryStub stub = null;
        private GenreRepositoryStub genreStub = null;
        private List<Game> gameList = null;
        private List<Genre> genreList = null;
        private Map<Long, Genre> genreMap = null;

        @Before
        public void setUp() {

            stub = new GameRepositoryStub();
            gameList = GameSearchDataGenerator.creaeteGames(200);
            gameList.forEach(stub::save);

            genreStub = new GenreRepositoryStub();
            genreList = GenreListDataGenerator.createGenreList(5);
            genreMap = genreList.stream()
                    .peek(genreStub::save)
                    .collect(Collectors.toMap(e -> e.getId().getValue(), v -> v));

            sut = new Games(new GameSearch(stub), new GenreSearch(genreStub));
        }

        @Test
        public void 検索条件は未入力_リストは20件表示される() {

            final Viewable viewable = sut.view();

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_LIST));
            assertThat(viewable.getModel(), is(instanceOf(GameListView.class)));

            final GameListView actualView = (GameListView) viewable.getModel();

            final GameSearchConditionBean actualCondition = actualView.getCondition();
            assertThat(actualCondition.getTitle(), is(nullValue()));
            assertThat(actualCondition.getCeroRating(), is(nullValue()));
            assertThat(actualCondition.getGenreId(), is(nullValue()));
            assertThat(actualCondition.getPageNumber(), is(nullValue()));
            TestTool.assertRatings(actualCondition.getRatings());
            TestTool.assertGenres(actualCondition.getGenres(), genreList);

            final List<GameBean> actualGames = actualView.getGames();
            assertThat(actualGames.size(), is(20));

            final int lastIndex = gameList.size() - 1;
            IntStream.range(0, actualGames.size()).forEach(i -> {
                final GameBean actualGame = actualGames.get(i);
                final Game expectedGame = gameList.get(lastIndex - i);
                final Genre expectedGenre = genreMap.get(expectedGame.getGenreId().getValue());

                assertThat(actualGame.getGameId(), is(expectedGame.getId().getValue()));
                assertThat(actualGame.getTitle(), is(expectedGame.getTitle().getValue()));
                assertThat(actualGame.getGenreId(), is(expectedGenre.getId().getValue()));
                assertThat(actualGame.getGenreName(), is(expectedGenre.getName().getValue()));
            });

            final PagerInfo actualPager = actualView.getPager();

            assertThat(actualPager.getCurrentPage(), is(1));

            final int[] actualPageNumbers = actualPager.getPages();
            assertThat(actualPageNumbers.length, is(10));

            for (int i = 0; i < 10; i++) {

                assertThat(actualPageNumbers[i], is(i + 1));
            }

            assertThat(actualPager.isPrevActive(), is(false));
            assertThat(actualPager.isNextActive(), is(true));
        }
    }

    public static class searchのテスト {

        private Games sut = null;
        private GameRepositoryStub stub = null;
        private GenreRepositoryStub genreStub = null;
        private List<Game> gameList = null;
        private List<Genre> genreList = null;
        private Map<Long, Genre> genreMap = null;

        @Before
        public void setUp() {

            stub = new GameRepositoryStub();
            gameList = GameSearchDataGenerator.creaeteGames(1000);
            gameList.forEach(stub::save);

            genreStub = new GenreRepositoryStub();
            genreList = GenreListDataGenerator.createGenreList(5);
            genreMap = genreList.stream()
                    .peek(genreStub::save)
                    .collect(Collectors.toMap(e -> e.getId().getValue(), v -> v));

            sut = new Games(new GameSearch(stub), new GenreSearch(genreStub));
        }

        @Test
        public void 検索条件にマッチするデータが取得できる() {

            final GameSearchConditionBean searchParam = new GameSearchConditionBean();
            searchParam.setTitle("タイトル25");
            searchParam.setCeroRating(CeroRating.A);
            searchParam.setGenreId(2l);

            final Viewable viewable = sut.search(searchParam);

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_LIST));
            assertThat(viewable.getModel(), is(instanceOf(GameListView.class)));

            final GameListView actualView = (GameListView) viewable.getModel();

            final GameSearchConditionBean actualCondition = actualView.getCondition();
            assertThat(actualCondition.getTitle(), is(searchParam.getTitle()));
            assertThat(actualCondition.getCeroRating(), is(searchParam.getCeroRating()));
            assertThat(actualCondition.getGenreId(), is(searchParam.getGenreId()));
            assertThat(actualCondition.getPageNumber(), is(nullValue()));
            TestTool.assertRatings(actualCondition.getRatings());
            TestTool.assertGenres(actualCondition.getGenres(), genreList);

            final List<GameBean> actualGames = actualView.getGames();
            assertThat(actualGames.size(), is(1));

            final GameBean actualGame = actualGames.get(0);
            final Game expectedGame = gameList.get(24);
            final Genre expectedGenre = genreMap.get(expectedGame.getGenreId().getValue());

            assertThat(actualGame.getGameId(), is(expectedGame.getId().getValue()));
            assertThat(actualGame.getTitle(), is(expectedGame.getTitle().getValue()));
            assertThat(actualGame.getGenreId(), is(expectedGenre.getId().getValue()));
            assertThat(actualGame.getGenreName(), is(expectedGenre.getName().getValue()));

            final PagerInfo actualPager = actualView.getPager();

            assertThat(actualPager.getCurrentPage(), is(1));

            final int[] actualPageNumbers = actualPager.getPages();
            assertThat(actualPageNumbers.length, is(1));

            assertThat(actualPager.isPrevActive(), is(false));
            assertThat(actualPager.isNextActive(), is(false));
        }

        @Test
        public void 指定したページの情報が取得できる() {

            final GameSearchConditionBean searchParam = new GameSearchConditionBean();
            searchParam.setPageNumber(15);
            final Viewable viewable = sut.search(searchParam);

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_LIST));
            assertThat(viewable.getModel(), is(instanceOf(GameListView.class)));

            final GameListView actualView = (GameListView) viewable.getModel();

            final GameSearchConditionBean actualCondition = actualView.getCondition();
            assertThat(actualCondition.getTitle(), is(nullValue()));
            assertThat(actualCondition.getCeroRating(), is(nullValue()));
            assertThat(actualCondition.getGenreId(), is(nullValue()));
            assertThat(actualCondition.getPageNumber(), is(15));
            TestTool.assertRatings(actualCondition.getRatings());
            TestTool.assertGenres(actualCondition.getGenres(), genreList);

            final List<GameBean> actualGames = actualView.getGames();
            assertThat(actualGames.size(), is(20));

            final int firstIndex = gameList.size() - (20 * (15 - 1));
            IntStream.range(0, actualGames.size()).forEach(i -> {
                final GameBean actualGame = actualGames.get(i);
                final Game expectedGame = gameList.get(firstIndex - i);

                assertThat(actualGame.getGameId(), is(expectedGame.getId().getValue()));
            });

            final PagerInfo actualPager = actualView.getPager();

            assertThat(actualPager.getCurrentPage(), is(15));

            final int[] actualPageNumbers = actualPager.getPages();
            assertThat(actualPageNumbers.length, is(10));

            for (int i = 0; i < 10; i++) {

                assertThat(actualPageNumbers[i], is(i + 11));
            }

            assertThat(actualPager.isPrevActive(), is(true));
            assertThat(actualPager.isNextActive(), is(true));
        }
    }
}