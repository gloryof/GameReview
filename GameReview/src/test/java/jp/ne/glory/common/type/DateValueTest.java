package jp.ne.glory.common.type;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class DateValueTest {

    public static class emptyメソッドのテスト {

        @Test
        public void 空のDateValueオブジェクトが返却される() {

            final DateValue actual = DateValue.empty();

            assertThat(actual.getValue(), is(nullValue()));
        }
    }

    public static class LocalDateの値が設定されている場合 {

        private DateValue sut = null;

        private final static LocalDate INIT_VALUE = LocalDate.of(2014, 7, 7);

        @Before
        public void setUp() {

            sut = new DateValue(INIT_VALUE);
        }

        @Test
        public void getValueで初期値が返却される() {

            assertThat(sut.getValue(), is(INIT_VALUE));
        }

        @Test
        public void formatで日付形式で返却される() {

            assertThat(sut.format(), is("2014/07/07"));
        }

        @Test
        public void isSameValueに同じ値を渡すとtrueが返却される() {

            assertThat(sut.isSamveValue(new DateValue(INIT_VALUE)), is(true));
        }

        @Test
        public void isSameValueに異なる値を渡すとfalseが返却される() {

            assertThat(sut.isSamveValue(new DateValue(2014, 8, 9)), is(false));            
        }

        @Test
        public void isSameValueにNullを渡すとfalseが返却される() {

            assertThat(sut.isSamveValue(null), is(false));            
        }
    }

    public static class intの値が設定されている場合 {

        private DateValue sut = null;

        private static final int YEAR_VALUE = 2014;
        private static final int MONTH_VALUE = 8;
        private static final int DAY_VALUE = 9;

        @Before
        public void setUp() {

            sut = new DateValue(YEAR_VALUE, MONTH_VALUE, DAY_VALUE);
        }

        @Test
        public void getValueで初期値が返却される() {

            assertThat(sut.getValue(), is(LocalDate.of(YEAR_VALUE, MONTH_VALUE, DAY_VALUE)));
        }

        @Test
        public void formatで日付形式で返却される() {

            assertThat(sut.format(), is("2014/08/09"));
        }
    }


    public static class Nullが設定されている場合 {

        private DateValue sut = null;

        @Before
        public void setUp() {

            sut = new DateValue(null);
        }

        @Test
        public void getValueでNullが返却される() {

            assertThat(sut.getValue(), is(nullValue()));
        }

        @Test
        public void formatでブランクで返却される() {

            assertThat(sut.format(), is(""));
        }

        @Test
        public void isSameValueに値を渡すとfalseが返却される() {

            assertThat(sut.isSamveValue(new DateValue(2014, 8, 9)), is(false));            
        }

        @Test
        public void isSameValueにNullを渡すとfalseが返却される() {

            assertThat(sut.isSamveValue(null), is(false));            
        }
    }
}
