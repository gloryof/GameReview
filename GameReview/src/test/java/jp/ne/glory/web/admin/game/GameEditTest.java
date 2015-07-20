
package jp.ne.glory.web.admin.game;

import java.net.URI;
import java.util.List;
import java.util.stream.IntStream;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.game.GameRegister;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.test.game.search.GameSearchDataGenerator;
import jp.ne.glory.test.genre.list.GenreListDataGenerator;
import jp.ne.glory.test.util.TestUtil;
import jp.ne.glory.ui.admin.game.GameEditView;
import jp.ne.glory.ui.genre.GenreBean;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class GameEditTest {

    public static class viewのテスト {

        private GameEdit sut = null;
        private GameRepositoryStub stub = null;
        private GenreRepositoryStub genreStub = null;
        private List<Game> gameList = null;
        private List<Genre> genreList = null;

        @Before
        public void setUp() {

            stub = new GameRepositoryStub();
            gameList = GameSearchDataGenerator.creaeteGames(200);
            gameList.forEach(stub::save);

            genreStub = new GenreRepositoryStub();
            genreList = GenreListDataGenerator.createGenreList(5);
            genreList.forEach(genreStub::save);

            sut = new GameEdit(new GameRegister(stub), new GameSearch(stub), new GenreSearch(genreStub));
        }

        @Test
        public void ゲームIDに紐づく編集画面が表示される() {

            final Long expectedLongGameId = 5l;
            final GameId expectedGameId = new GameId(expectedLongGameId);
            final Response actualResponse = sut.view(expectedLongGameId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable actualViewable = (Viewable) actualResponse.getEntity();

            assertThat(actualViewable.getTemplateName(), is(PagePaths.GAME_EDIT));
            assertThat(actualViewable.getModel(), is(instanceOf(GameEditView.class)));

            final GameEditView actualView = (GameEditView) actualViewable.getModel();
            final Game expectedGame = stub.findBy(expectedGameId).get();

            assertThat(actualView.getGameId(), is(expectedGame.getId().getValue()));
            assertThat(actualView.getGenreId(), is(expectedGame.getGenreId().getValue()));
            assertThat(actualView.getTitle(), is(expectedGame.getTitle().getValue()));
            assertThat(actualView.getUrl(), is(expectedGame.getUrl().getValue()));
            assertThat(actualView.getCeroRating(), is(expectedGame.getCeroRating()));

            final CeroRating[] expcetedRatings = CeroRating.values();

            final int exptectedLength = expcetedRatings.length - 1;
            assertThat(actualView.getRatings().size(), is(exptectedLength));
            for (int i = 0; i < exptectedLength; i++) {

                final CeroRating expectedRating = expcetedRatings[i];
                final CeroRating actualRating = actualView.getRatings().get(i);

                assertThat(actualRating, is(expectedRating));
            }

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

    public static class completeEditのテスト {

        private GameEdit sut = null;
        private GameRepositoryStub stub = null;
        private GenreRepositoryStub genreStub = null;
        private List<Game> gameList = null;
        private List<Genre> genreList = null;

        @Before
        public void setUp() {

            stub = new GameRepositoryStub();
            gameList = GameSearchDataGenerator.creaeteGames(200);
            gameList.forEach(stub::save);

            genreStub = new GenreRepositoryStub();
            genreList = GenreListDataGenerator.createGenreList(5);
            genreList.forEach(genreStub::save);

            sut = new GameEdit(new GameRegister(stub), new GameSearch(stub), new GenreSearch(genreStub));

        }

        @Test
        public void 正常に編集できた場合_詳細画面を表示する() {

            final long excepctedGameIdValue = 5l;
            final GameEditView paramView = new GameEditView();
            paramView.setGameId(excepctedGameIdValue);
            paramView.setGenreId(1l);
            paramView.setTitle("タイトル");
            paramView.setCeroRating(CeroRating.A);
            paramView.setUrl("https://google.com/");

            final Response actualResponse = sut.completeEdit(excepctedGameIdValue, paramView);

            final URI expectedUri = UriBuilder.fromResource(GameDetail.class).resolveTemplate("gameId", excepctedGameIdValue).build();
            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(expectedUri));

            final Game actualGame = stub.findBy(new GameId(excepctedGameIdValue)).get();
            assertThat(actualGame.getId().getValue(), is(excepctedGameIdValue));
            assertThat(actualGame.getGenreId().getValue(), is(paramView.getGenreId()));
            assertThat(actualGame.getTitle().getValue(), is(paramView.getTitle()));
            assertThat(actualGame.getUrl().getValue(), is(paramView.getUrl()));
            assertThat(actualGame.getCeroRating(), is(paramView.getCeroRating()));
        }

        @Test
        public void 入力エラーがある場合_編集画面を表示する() {

            final long paramGameId = 5l;
            final GameEditView expectedView = new GameEditView();
            expectedView.setGameId(paramGameId);
            expectedView.setGenreId(10l);
            expectedView.setTitle(TestUtil.repeat("あ", 120));
            expectedView.setCeroRating(CeroRating.A);
            expectedView.setUrl("https://google.com/");

            final Response actualResponse = sut.completeEdit(paramGameId, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_EDIT));
            assertThat(viewable.getModel(), is(instanceOf(GameEditView.class)));

            final GameEditView actualView = (GameEditView) viewable.getModel();

            assertThat(actualView.getGameId(), is(paramGameId));
            assertThat(actualView.getGenreId(), is(expectedView.getGenreId()));
            assertThat(actualView.getTitle(), is(expectedView.getTitle()));
            assertThat(actualView.getUrl(), is(expectedView.getUrl()));
            assertThat(actualView.getCeroRating(), is(expectedView.getCeroRating()));

            final CeroRating[] expcetedRatings = CeroRating.values();

            final int exptectedLength = expcetedRatings.length - 1;
            assertThat(actualView.getRatings().size(), is(exptectedLength));
            for (int i = 0; i < exptectedLength; i++) {

                final CeroRating expectedRating = expcetedRatings[i];
                final CeroRating actualRating = actualView.getRatings().get(i);

                assertThat(actualRating, is(expectedRating));
            }

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
        public void URLのゲームIDと入力値のゲームIDがとなる場合_Badリクエストエラーになる() {

            final long paramGameId = 5L;
            final GameEditView expectedView = new GameEditView();
            expectedView.setGenreId(paramGameId);

            final Response actualResponse = sut.completeEdit(10l, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 入力値のゲームIDが未設定の場合_Badリクエストエラーになる() {

            final long paramGameId = 5L;
            final GameEditView expectedView = new GameEditView();

            final Response actualResponse = sut.completeEdit(paramGameId, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 指定したゲームIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long paramGameId = Long.MAX_VALUE;
            final GameEditView expectedView = new GameEditView();
            expectedView.setGameId(paramGameId);

            final Response actualResponse = sut.completeEdit(paramGameId, expectedView);

            final String base = UriBuilder.fromResource(GameDetail.class).toTemplate();
            final String append = UriBuilder.fromMethod(GameDetail.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(paramGameId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }
}