package jp.ne.glory.domain.game.entity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import jp.ne.groly.domain.game.entity.Game;
import jp.ne.groly.domain.game.value.CeroRating;
import jp.ne.groly.domain.game.value.GameId;
import jp.ne.groly.domain.game.value.SiteUrl;
import jp.ne.groly.domain.game.value.Title;
import jp.ne.groly.domain.genre.value.GenreId;
import org.junit.Before;
import org.junit.Test;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;


@RunWith(Enclosed.class)
public class GameTest {

    public static class 全ての値が正常に設定されている場合 {

        private Game sut = null;

        private static final GameId INIT_GAME_ID = new GameId(12L);
        private static final Title INIT_TITLE = new Title("テストタイトル");

        @Before
        public void setUp() {

            sut = new Game(INIT_GAME_ID, INIT_TITLE);
            sut.url = new SiteUrl("http://test.co.jp/index");
            sut.genreId = new GenreId(10L);
            sut.ceroRating = CeroRating.A;
        }

        @Test
        public void コンストラクタで設定した値がプロパティに設定されている() {

            assertThat(sut.id.isSame(INIT_GAME_ID), is(true));
            assertThat(sut.title.value, is(INIT_TITLE.value));
        }

        @Test
        public void isRegisteredにtrueが設定されている() {

            assertThat(sut.isRegistered(), is(true));
        }
    }

    public static class コンストラクタに初期値が設定されている場合 {
        
        private Game sut = null;

        private static final GameId INIT_GAME_ID = GameId.notNumberingValue();
        private static final Title INIT_TITLE =  Title.empty();

        @Before
        public void setUp() {

            sut = new Game(INIT_GAME_ID, INIT_TITLE);
        }

        @Test
        public void 全てに初期値が設定されている() {

            assertThat(sut.id.isSame(INIT_GAME_ID), is(true));
            assertThat(sut.title.value, is(INIT_TITLE.value));
            assertThat(sut.url.value, is(SiteUrl.empty().value));
            assertThat(sut.ceroRating, is(CeroRating.Empty));
            assertThat(sut.genreId.isSame(GenreId.notNumberingValue()), is(true));
        }

        @Test
        public void isRegisteredにfalseが設定されている() {

            assertThat(sut.isRegistered(), is(false));
        }
    }

}
