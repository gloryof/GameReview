package jp.ne.glory.domain.game.flow;

import java.util.Optional;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.value.GenreId;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class GameRegisterTest {

    public static class registerのテスト {

        private GameRegister sut = null;
        private GameRepositoryStub repoStub = null;

        @Before
        public void setUp() {

            repoStub = new GameRepositoryStub();
            sut = new GameRegister(repoStub);
        }

        @Test
        public void 正常な値が入力されていれば保存される() {

            final Game game = new Game(GameId.notNumberingValue(), new Title("タイトル"));
            game.url = new SiteUrl("http://test.co.jp/index");
            game.genreId = new GenreId(10L);
            game.ceroRating = CeroRating.A;

            final GameRegisterResult result = sut.register(game);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(false));

            final Optional<Game> savedUser = repoStub.findBy(result.registeredGameId);

            assertThat(savedUser.isPresent(), is(true));
        }

        @Test
        public void 入力に不正がある場合エラーになる() {

            final Game game = new Game(GameId.notNumberingValue(), Title.empty());

            final GameRegisterResult result = sut.register(game);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(true));
        }
    }

    public static class finishEditのテスト {

        private GameRegister sut = null;
        private GameRepositoryStub repoStub = null;

        @Before
        public void setUp() {

            repoStub = new GameRepositoryStub();
            sut = new GameRegister(repoStub);
        }

        @Test
        public void 正常な値が入力されていれば保存される() {

            final Game game = new Game(new GameId(100L), new Title("タイトル"));
            game.url = new SiteUrl("http://test.co.jp/index");
            game.genreId = new GenreId(10L);
            game.ceroRating = CeroRating.A;

            final GameRegisterResult result = sut.finishEdit(game);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(false));

            final Optional<Game> savedUser = repoStub.findBy(result.registeredGameId);

            assertThat(savedUser.isPresent(), is(true));
        }

        @Test
        public void 入力に不正がある場合エラーになる() {

            final Game game = new Game(GameId.notNumberingValue(), Title.empty());

            final GameRegisterResult result = sut.finishEdit(game);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(true));
        }

        @Test
        public void IDが設定されていない場合エラーになる() {

            final Game game = new Game(GameId.notNumberingValue(), Title.empty());
            game.url = new SiteUrl("http://test.co.jp/index");
            game.genreId = new GenreId(10L);
            game.ceroRating = CeroRating.A;

            final GameRegisterResult result = sut.finishEdit(game);

            final ValidateErrors errors = result.errors;
            assertThat(errors.hasError(), is(true));
        }
    }
}
