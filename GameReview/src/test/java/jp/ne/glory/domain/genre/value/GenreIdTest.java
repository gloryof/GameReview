package jp.ne.glory.domain.genre.value;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class GenreIdTest {

    public static class notNumberingValueのテスト {

        @Test
        public void 値が0で未採番の値が返却される() {

            final GenreId actual  = GenreId.notNumberingValue();

            assertThat(actual.getValue(), is(0L));
            assertThat(actual.isSetValue(), is(false));
        }
    }
}
