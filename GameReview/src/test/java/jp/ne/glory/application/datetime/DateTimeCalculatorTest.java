package jp.ne.glory.application.datetime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class DateTimeCalculatorTest {

    public static class getCurrentDateTimeのテスト {

        private DateTimeCalculator sut = null;

        @Before
        public void setUp() {

            sut = new DateTimeCalculator();
        }

        @Test
        public void 現在の日時が取得できる() {

            final LocalDateTime currentTime = LocalDateTime.now();
            final LocalDateTime actualTime = sut.getCurrentDateTime().getValue();

            final long currentMills = currentTime.toEpochSecond(ZoneOffset.UTC);
            final long actualMills = actualTime.toEpochSecond(ZoneOffset.UTC);

            final long compareValue = Math.abs(currentMills - actualMills);
            assertThat(compareValue <= 1000, is(true));
        }
    }
}
