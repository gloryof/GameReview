
package jp.ne.glory.web.admin.game;

import java.net.URI;
import java.util.List;
import java.util.stream.IntStream;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.game.GameRegister;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class GameCreateTest {

    public static class viewのテスト {

        private GameCreate sut = null;
        private GenreRepositoryStub genreStub = null;
        private List<Genre> genreList = null;

        @Before
        public void setUp() {

            genreStub = new GenreRepositoryStub();
            genreList = GenreListDataGenerator.createGenreList(10);
            genreList.forEach(genreStub::save);

            sut = new GameCreate(new GameRegister(new GameRepositoryStub()), new GenreSearch(genreStub));
        }

        @Test
        public void 新規登録画面が表示される() {

            final Viewable actualViewable = sut.view();

            assertThat(actualViewable.getTemplateName(), is(PagePaths.GAME_CREATE));
            assertThat(actualViewable.getModel(), is(instanceOf(GameEditView.class)));

            final GameEditView actualView = (GameEditView) actualViewable.getModel();

            assertThat(actualView.getGameId(), is(nullValue()));
            assertThat(actualView.getGenreId(), is(nullValue()));
            assertThat(actualView.getTitle(), is(nullValue()));
            assertThat(actualView.getUrl(), is(nullValue()));
            assertThat(actualView.getCeroRating(), is(nullValue()));

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
    }

    public static class createのテスト {

        private GameCreate sut = null;
        private GameRepositoryStub stub = null;
        private GenreRepositoryStub genreStub = null;
        private List<Genre> genreList = null;


        @Before
        public void setUp() {

            stub = new GameRepositoryStub();
            final GameRegister register = new GameRegister(stub);

            genreStub = new GenreRepositoryStub();
            genreList = GenreListDataGenerator.createGenreList(10);
            genreList.forEach(genreStub::save);

            sut = new GameCreate(register, new GenreSearch(genreStub));
        }

        @Test
        public void 正常に登録できた場合_詳細画面を表示する() {

            final GameEditView paramView = new GameEditView();
            paramView.setGenreId(10l);
            paramView.setTitle("タイトル");
            paramView.setCeroRating(CeroRating.A);
            paramView.setUrl("https://google.com/");

            final long excepctedGameIdValue = stub.getCurrentSequence();

            final Response actualResponse = sut.create(paramView);

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
        public void 入力エラーがある場合_作成画面を表示する() {

            final GameEditView expectedView = new GameEditView();
            expectedView.setGenreId(10l);
            expectedView.setTitle(TestUtil.repeat("あ", 120));
            expectedView.setCeroRating(CeroRating.A);
            expectedView.setUrl("https://google.com/");

            final Response actualResponse = sut.create(expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.GAME_CREATE));
            assertThat(viewable.getModel(), is(instanceOf(GameEditView.class)));

            final GameEditView actualView = (GameEditView) viewable.getModel();

            assertThat(actualView.getGameId(), is(nullValue()));
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
    }

}