
package jp.ne.glory.application.game;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;
import jp.ne.glory.domain.game.value.search.GameSearchResults;
import jp.ne.glory.test.game.search.GameSearchDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class GameSearchTest {

    public static class getAllGamesのテスト {

        private GameSearch sut = null;
        private GameRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new GameRepositoryStub();
            GameSearchDataGenerator.creaeteUsers(10).stream().forEach(stub::save);

            sut = new GameSearch(stub);
        }

        @Test
        public void 全てのゲームが取得される() {

            final List<Game> expectedGames = stub.findAll();
            final List<Game> actualGames = sut.getAllGames();
            final int actualSize = actualGames.size();

            assertThat(actualSize, is(expectedGames.size()));

            IntStream.range(0, actualSize).forEach(i -> {

                final Game actual = actualGames.get(i);
                final Game expected = expectedGames.get(i);

                assertThat(actual.getId().isSame(expected.getId()), is(true));
                assertThat(actual.getTitle().getValue(), is(expected.getTitle().getValue()));
                assertThat(actual.getCeroRating(), is(expected.getCeroRating()));
                assertThat(actual.getGenreId().isSame(expected.getGenreId()), is(true));
                assertThat(actual.getUrl().getValue(), is(expected.getUrl().getValue()));
            });
        }
    }

    public static class searchByのテスト {

        private GameSearch sut = null;
        private GameRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new GameRepositoryStub();
            GameSearchDataGenerator.creaeteUsers(10).stream().forEach(stub::save);

            sut = new GameSearch(stub);
        }

        @Test
        public void 該当するジャンルIDがある場合はジャンルが取得できる() {

            final GameId expectedGameId = new GameId(4L);

            final Optional<Game> actualGameOpt = sut.searchBy(expectedGameId);

            assertThat(actualGameOpt.isPresent(), is(true));

            final Game actual = actualGameOpt.get();

            assertThat(actual.getId().isSame(expectedGameId), is(true));
            assertThat(actual.getTitle().getValue(), is("タイトル" + expectedGameId.getValue()));
            assertThat(actual.getCeroRating(), is(CeroRating.D));
            assertThat(actual.getGenreId().getValue(), is(2l));
            assertThat(actual.getUrl().getValue(), is("http://localhost:8080/test/4"));
        }

        @Test
        public void 該当するジャンルIDがない場合_Optionalの中身は空() {

            final GameId expectedGameId = new GameId(Long.MAX_VALUE);

            final Optional<Game> actualGenreOpt = sut.searchBy(expectedGameId);

            assertThat(actualGenreOpt.isPresent(), is(false));
        }
    }

    public static class searchのテスト {

        private GameSearch sut = null;
        private List<Game> gameList = null;
        private GameRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new GameRepositoryStub();
            gameList = GameSearchDataGenerator.creaeteUsers(100);
            gameList.stream().forEach(stub::save);

            sut = new GameSearch(stub);
        }

        @Test
        public void 検索条件なしの場合は全件がページングなしで返却される() {

            final GameSearchResults actualResult = sut.search(new GameSearchCondition());

            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(false));
            assertThat(actualResult.getAllCount(), is(100));

            final GameSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getTitle(), is(nullValue()));
            assertThat(actualCondition.getGenreId(), is(nullValue()));
            assertThat(actualCondition.getCeroRating(), is(nullValue()));

            assertThat(actualCondition.getLotNumber(), is(1));
            assertThat(actualCondition.getLotPerCount(), is(0));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Game> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(gameList.size()));
        }

        @Test
        public void 検索条件なし_1ページ5件で1ページ目を表示する場合__前ページなし_次ページありとなる() {

            final GameSearchCondition condition = new GameSearchCondition();
            condition.setLotNumber(1);
            condition.setLotPerCount(5);

            final GameSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(true));
            assertThat(actualResult.hasPrevLot(), is(false));
            assertThat(actualResult.getAllCount(), is(100));

            final GameSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getTitle(), is(nullValue()));
            assertThat(actualCondition.getGenreId(), is(nullValue()));
            assertThat(actualCondition.getCeroRating(), is(nullValue()));

            assertThat(actualCondition.getLotNumber(), is(1));
            assertThat(actualCondition.getLotPerCount(), is(5));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Game> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(5));
        }

        @Test
        public void 検索条件なし_1ページ5件で10ページ目を表示する場合__前ページあり_次ページありとなる() {

            final GameSearchCondition condition = new GameSearchCondition();
            condition.setLotNumber(10);
            condition.setLotPerCount(5);

            final GameSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(true));
            assertThat(actualResult.hasPrevLot(), is(true));
            assertThat(actualResult.getAllCount(), is(100));

            final GameSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getTitle(), is(nullValue()));
            assertThat(actualCondition.getGenreId(), is(nullValue()));
            assertThat(actualCondition.getCeroRating(), is(nullValue()));

            assertThat(actualCondition.getLotNumber(), is(10));
            assertThat(actualCondition.getLotPerCount(), is(5));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Game> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(5));
        }

        @Test
        public void 検索条件なし_1ページ5件で20ページ目を表示する場合__前ページあり_次ページなしとなる() {

            final GameSearchCondition condition = new GameSearchCondition();
            condition.setLotNumber(20);
            condition.setLotPerCount(5);

            final GameSearchResults actualResult = sut.search(condition);

            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(true));
            assertThat(actualResult.getAllCount(), is(100));

            final GameSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getTitle(), is(nullValue()));
            assertThat(actualCondition.getGenreId(), is(nullValue()));
            assertThat(actualCondition.getCeroRating(), is(nullValue()));

            assertThat(actualCondition.getLotNumber(), is(20));
            assertThat(actualCondition.getLotPerCount(), is(5));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Game> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(5));
        }

        @Test
        public void 検索条件を入力するに一致するゲームが取得できる() {

            final GameSearchCondition expectedCondition = new GameSearchCondition();
            final Game expectedGame = gameList.get(12);
            expectedCondition.setTitle(expectedGame.getTitle());
            expectedCondition.setGenreId(expectedGame.getGenreId());
            expectedCondition.setCeroRating(expectedGame.getCeroRating());

            final GameSearchResults actualResult = sut.search(expectedCondition);

            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(false));
            assertThat(actualResult.getAllCount(), is(1));

            final GameSearchCondition actualCondition = actualResult.getCondition();
            assertThat(actualCondition.getTitle().getValue(), is(expectedCondition.getTitle().getValue()));
            assertThat(actualCondition.getGenreId().getValue(), is(expectedCondition.getGenreId().getValue()));
            assertThat(actualCondition.getCeroRating(), is(expectedCondition.getCeroRating()));

            assertThat(actualCondition.getLotNumber(), is(1));
            assertThat(actualCondition.getLotPerCount(), is(0));
            assertThat(actualCondition.getTargetCount(), is(0));

            final List<Game> actualList = actualResult.getResults();
            assertThat(actualList.size(), is(1));

            final Game actualGame = actualList.get(0);

            assertThat(actualGame.getTitle().getValue(), is(expectedCondition.getTitle().getValue()));
            assertThat(actualGame.getGenreId().getValue(), is(expectedCondition.getGenreId().getValue()));
            assertThat(actualGame.getCeroRating(), is(expectedCondition.getCeroRating()));
        }
    }
}