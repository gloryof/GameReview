
package jp.ne.glory.web.admin.game;

import java.util.List;
import java.util.stream.IntStream;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.test.game.search.GameSearchDataGenerator;
import jp.ne.glory.ui.admin.game.GameBean;
import jp.ne.glory.ui.admin.game.GameListView;
import jp.ne.glory.ui.admin.game.GameSearchConditionBean;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class GamesTest {

    public static class viewのテスト {

        private Games sut = null;
        private GameRepositoryStub stub = null;
        private List<Game> gameList = null;

        @Before
        public void setUp() {

            stub = new GameRepositoryStub();
            gameList = GameSearchDataGenerator.creaeteGames(100);
            gameList.forEach(stub::save);

            sut = new Games(new GameSearch(stub));
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

            final List<GameBean> actualGames = actualView.getGames();
            assertThat(actualGames.size(), is(20));

            final int lastIndex = gameList.size() - 1;
            IntStream.range(0, actualGames.size()).forEach(i -> {
                final GameBean actualGame = actualGames.get(i);
                final Game expectedGame = gameList.get(lastIndex - i);

                assertThat(actualGame.getGameId(), is(expectedGame.getId().getValue()));
                assertThat(actualGame.getTitle(), is(expectedGame.getTitle().getValue()));
            });
        }
    }

}