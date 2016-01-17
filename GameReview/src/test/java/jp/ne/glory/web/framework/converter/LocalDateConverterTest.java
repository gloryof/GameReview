package jp.ne.glory.web.framework.converter;

import java.time.LocalDate;
import javax.ws.rs.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class LocalDateConverterTest {

    public static class fromStringのテスト {

        private LocalDateConverter sut = null;

        @Before
        public void setUp() {

            sut = new LocalDateConverter();
        }

        @Test
        public void Nullを渡すとNullが返却される() {

            assertThat(sut.fromString(null), is(nullValue()));
        }

        @Test
        public void 空文字を渡すとNullが返却される() {

            assertThat(sut.fromString(""), is(nullValue()));
        }

        @Test
        public void 数字のみ20160102を渡すと2016年1月2日が返却される() {

            final LocalDate expectedDate = LocalDate.of(2016, 1, 2);
            assertThat(sut.fromString("20160102"), is(expectedDate));
        }

        @Test
        public void 数字のみ20161112を渡すと2016年11月12日が返却される() {

            final LocalDate expectedDate = LocalDate.of(2016, 11, 12);
            assertThat(sut.fromString("20161112"), is(expectedDate));
        }

        @Test
        public void スラッシュ区切り20160102を渡すと2016年1月2日が返却される() {

            final LocalDate expectedDate = LocalDate.of(2016, 1, 2);
            assertThat(sut.fromString("2016/01/02"), is(expectedDate));
        }

        @Test
        public void スラッシュ区切り20161112を渡すと2016年11月12日が返却される() {

            final LocalDate expectedDate = LocalDate.of(2016, 11, 12);
            assertThat(sut.fromString("2016/11/12"), is(expectedDate));
        }

        @Test
        public void ハイフン区切り20160102を渡すと2016年1月2日が返却される() {

            final LocalDate expectedDate = LocalDate.of(2016, 1, 2);
            assertThat(sut.fromString("2016-01-02"), is(expectedDate));
        }

        @Test
        public void ハイフン区切り20161112を渡すと2016年11月12日が返却される() {

            final LocalDate expectedDate = LocalDate.of(2016, 11, 12);
            assertThat(sut.fromString("2016-11-12"), is(expectedDate));
        }

        @Test
        public void うるう年20160229を渡すと2016年2月29日が返却される() {

            final LocalDate expectedDate = LocalDate.of(2016, 2, 29);
            assertThat(sut.fromString("20160229"), is(expectedDate));
        }

        @Test(expected = BadRequestException.class)
        public void スラッシュ区切りで不正な日付の場合はBadRequestExceptionがスローされる() {

            sut.fromString("2015/02/29");
        }

        @Test(expected = BadRequestException.class)
        public void ハイフン区切りで不正な日付の場合はBadRequestExceptionがスローされる() {

            sut.fromString("2015-02-29");
        }

        @Test(expected = BadRequestException.class)
        public void 数字のみで不正な日付の場合はBadRequestExceptionがスローされる() {

            sut.fromString("20150229");
        }

        @Test(expected = BadRequestException.class)
        public void 数字以外が含まれている場合はBadRequestExceptionがスローされる() {

            sut.fromString("2015a229");
        }
    }

    public static class toStringのテスト {

        private LocalDateConverter sut = null;

        @Before
        public void setUp() {

            sut = new LocalDateConverter();
        }

        @Test
        public void Nullを渡すとNullが返却される() {

            assertThat(sut.fromString(null), is(nullValue()));
        }

        @Test
        public void 日付が2016年1月2日の場合は20160102のハイフン区切りで表示される() {

            final LocalDate dateValue = LocalDate.of(2016, 1, 2);
            assertThat(sut.toString(dateValue), is("2016-01-02"));
        }

        @Test
        public void 日付が2016年11月12日の場合は20161112のハイフン区切りで表示される() {

            final LocalDate dateValue = LocalDate.of(2016, 11, 12);
            assertThat(sut.toString(dateValue), is("2016-11-12"));
        }
    }

    @RunWith(Enclosed.class)
    public static class 相互変換 {

        @RunWith(Enclosed.class)
        public static class Stringを起点とした変換 {

            private LocalDateConverter sut = null;

            @Before
            public void setUp() {

                sut = new LocalDateConverter();
            }

            @Test
            public void 数値のみの場合はハイフン区切りになって戻ってくる() {

                final String baseValue = "20160109";
                final String expectedValue = "2016/01/09";
                final String actualValue = sut.toString(sut.fromString(baseValue));

                assertThat(actualValue, is(expectedValue));
            }

            @Test
            public void スラッシュ区切りの場合はハイフン区切りになって戻ってくる() {

                final String baseValue = "2016/01/09";
                final String expectedValue = "2016-01-09";
                final String actualValue = sut.toString(sut.fromString(baseValue));

                assertThat(actualValue, is(expectedValue));
            }

            @Test
            public void ハイフン区切りの_再変換で同じ値が帰ってくる() {

                final String expectedValue = "2016-01-09";
                final String actualValue = sut.toString(sut.fromString(expectedValue));

                assertThat(actualValue, is(expectedValue));
            }
        }

        @RunWith(Enclosed.class)
        public static class LocalDateを起点とした変換 {

            private LocalDateConverter sut = null;

            @Before
            public void setUp() {

                sut = new LocalDateConverter();
            }

            @Test
            public void 変換_再変換で同じ値が帰ってくる() {

                final LocalDate expectedValue = LocalDate.of(2016, 1, 9);
                final LocalDate actualValue = sut.fromString(sut.toString(expectedValue));

                assertThat(actualValue, is(expectedValue));
            }
        }
    }
}
