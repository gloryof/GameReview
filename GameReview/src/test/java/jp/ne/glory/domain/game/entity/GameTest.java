package jp.ne.glory.domain.game.entity;

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
public class GameTest {

    public static class 全ての値が正常に設定されている場合 {

        private Game sut = null;

        private static final GameId INIT_GAME_ID = new GameId(12L);

        @Before
        public void setUp() {

            sut = new Game(INIT_GAME_ID);
            sut.setTitle(new Title("テストタイトル"));
            sut.setUrl(new SiteUrl("http://test.co.jp/index"));
            sut.setGenreId(new GenreId(10L));
            sut.setCeroRating(CeroRating.A);
        }

        @Test
        public void コンストラクタで設定した値がプロパティに設定されている() {

            assertThat(sut.getId().isSame(INIT_GAME_ID), is(true));
        }

        @Test
        public void isRegisteredにtrueが設定されている() {

            assertThat(sut.isRegistered(), is(true));
        }
    }

    public static class コンストラクタに初期値が設定されている場合 {
        
        private Game sut = null;

        private static final GameId INIT_GAME_ID = GameId.notNumberingValue();

        @Before
        public void setUp() {

            sut = new Game(INIT_GAME_ID);
        }

        @Test
        public void 全てに初期値が設定されている() {

            assertThat(sut.getId().isSame(INIT_GAME_ID), is(true));
            assertThat(sut.getTitle().getValue(), is(Title.empty().getValue()));
            assertThat(sut.getUrl().getValue(), is(SiteUrl.empty().getValue()));
            assertThat(sut.getCeroRating(), is(CeroRating.Empty));
            assertThat(sut.getGenreId().isSame(GenreId.notNumberingValue()), is(true));
        }

        @Test
        public void isRegisteredにfalseが設定されている() {

            assertThat(sut.isRegistered(), is(false));
        }
    }

}
