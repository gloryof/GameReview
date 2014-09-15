package jp.ne.glory.domain.review.value;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class ScorePointTest {

    public static class getByIdのテスト {

        @Test
        public void 存在するIDで取得できる_パターン1() {

            final ScorePoint expetcted = ScorePoint.Point0;
            final ScorePoint actual = ScorePoint.getByTypeId(expetcted.typeId);

            assertThat(actual, is(expetcted));
        }

        @Test
        public void 存在するIDで取得できる_パターン2() {

            final ScorePoint expetcted = ScorePoint.Point5;
            final ScorePoint actual = ScorePoint.getByTypeId(expetcted.typeId);

            assertThat(actual, is(expetcted));
        }

        @Test
        public void 存在するIDで取得できる_パターン3() {

            final ScorePoint expetcted = ScorePoint.Exclued;
            final ScorePoint actual = ScorePoint.getByTypeId(expetcted.typeId);

            assertThat(actual, is(expetcted));
        }

        @Test
        public void 存在しないIDではNOT_INPUTが返却される() {

            final ScorePoint expetcted = ScorePoint.NotInput;
            final ScorePoint actual = ScorePoint.getByTypeId(-123);

            assertThat(actual, is(expetcted));
        }
    }
}
