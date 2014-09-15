package jp.ne.glory.domain.game.value;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import jp.ne.groly.domain.game.value.GameId;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class GameIdTest {

    public static class notNumberingValueのテスト {

        @Test
        public void 値が0で未採番の値が返却される() {

            final GameId actual  = GameId.notNumberingValue();

            assertThat(actual.value, is(0L));
            assertThat(actual.isSetValue, is(false));
        }
    }
}
