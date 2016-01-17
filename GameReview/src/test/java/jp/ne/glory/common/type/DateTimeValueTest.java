package jp.ne.glory.common.type;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class DateTimeValueTest {

    public static class emptyメソッドのテスト {

        @Test
        public void 空のDateTimeValueオブジェクトが返却される() {

            final DateTimeValue actual = DateTimeValue.empty();

            assertThat(actual.getValue(), is(nullValue()));
        }
    }

    public static class LocalDateの値が設定されている場合 {

        private DateTimeValue sut = null;

        private final static LocalDateTime INIT_VALUE = LocalDateTime.of(2014, 7, 8, 23, 48, 54, 123);

        @Before
        public void setUp() {

            sut = new DateTimeValue(INIT_VALUE);
        }

        @Test
        public void getValueで初期値が返却される() {

            assertThat(sut.getValue(), is(INIT_VALUE));
        }

        @Test
        public void formatで日付形式で返却される() {

            assertThat(sut.format(), is("2014-07-08 23:48:54"));
        }
    }

    public static class Nullが設定されている場合 {

        private DateTimeValue sut = null;

        @Before
        public void setUp() {

            sut = new DateTimeValue(null);
        }

        @Test
        public void getValueでNullが返却される() {

            assertThat(sut.getValue(), is(nullValue()));
        }

        @Test
        public void formatでブランクで返却される() {

            assertThat(sut.format(), is(""));
        }
    }
}
