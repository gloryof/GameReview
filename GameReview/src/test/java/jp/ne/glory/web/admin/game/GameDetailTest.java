package jp.ne.glory.web.admin.game;

import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.test.game.search.GameSearchDataGenerator;
import jp.ne.glory.test.genre.list.GenreListDataGenerator;
import jp.ne.glory.ui.admin.game.GameDetailView;
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
public class GameDetailTest {

    public static class viewのテスト {

        private GameDetail sut = null;
        private GameRepositoryStub gameRepStub = null;
        private GenreRepositoryStub genreRepStub = null;

        @Before
        public void setUp() {

            gameRepStub = new GameRepositoryStub();
            GameSearchDataGenerator.creaeteGames(10).forEach(gameRepStub::save);

            genreRepStub = new GenreRepositoryStub();
            GenreListDataGenerator.createGenreList(10).forEach(genreRepStub::save);

            final GameSearch gameSearch = new GameSearch(gameRepStub);
            final GenreSearch genreSearch = new GenreSearch(genreRepStub);

            sut = new GameDetail(gameSearch, genreSearch);
        }

        @Test
        public void 指定したゲームIDに紐付くゲームが取得できる() {

            final long expectedGameId = 4;

            final Response actualResponse = sut.view(expectedGameId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_DETAIL));
            assertThat(viewable.getModel(), is(instanceOf(GameDetailView.class)));

            final GameDetailView actualView = (GameDetailView) viewable.getModel();
            final Game expectedGame = gameRepStub.findBy(new GameId(expectedGameId)).get();
            final Genre expectedGenre = genreRepStub.findBy(expectedGame.getGenreId()).get();

            assertThat(actualView.getGameId(), is(expectedGame.getId().getValue()));
            assertThat(actualView.getTitle(), is(expectedGame.getTitle().getValue()));
            assertThat(actualView.getUrl(), is(expectedGame.getUrl().getValue()));
            assertThat(actualView.getGenreName(), is(expectedGenre.getName().getValue()));
        }

        @Test
        public void 指定したゲームIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long genreId = Long.MAX_VALUE;
            final Response actualResponse = sut.view(genreId);

            final String base = UriBuilder.fromResource(GameDetail.class).toTemplate();
            final String append = UriBuilder.fromMethod(GameDetail.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(genreId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }

    public static class notFoundのテスト {

        private GameDetail sut = null;
        private GameRepositoryStub gameRepStub = null;
        private GenreRepositoryStub genreRepStub = null;

        @Before
        public void setUp() {

            gameRepStub = new GameRepositoryStub();
            GameSearchDataGenerator.creaeteGames(10).forEach(gameRepStub::save);

            genreRepStub = new GenreRepositoryStub();
            GenreListDataGenerator.createGenreList(10).forEach(genreRepStub::save);

            final GameSearch gameSearch = new GameSearch(gameRepStub);
            final GenreSearch genreSearch = new GenreSearch(genreRepStub);

            sut = new GameDetail(gameSearch, genreSearch);
        }

        @Test
        public void エラー画面が表示される() {

            final Viewable viewable = sut.notFound();

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_NOT_FOUND));
            assertThat(viewable.getModel(), is(nullValue()));
        }
    }

}
