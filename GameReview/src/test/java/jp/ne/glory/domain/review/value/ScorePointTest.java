package jp.ne.glory.domain.review.value;

import java.util.List;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

    public static class fromStringのテスト {

        @Test
        public void 存在するIDで取得できる_パターン1() {

            final ScorePoint expected = ScorePoint.Point0;
            final ScorePoint actual = ScorePoint.fromString(String.valueOf(expected.typeId));

            assertThat(actual, is(expected));
        }

        @Test
        public void 存在するIDで取得できる_パターン2() {

            final ScorePoint expected = ScorePoint.Point5;
            final ScorePoint actual = ScorePoint.fromString(String.valueOf(expected.typeId));

            assertThat(actual, is(expected));
        }

        @Test
        public void 存在するIDで取得できる_パターン3() {

            final ScorePoint expected = ScorePoint.Exclued;
            final ScorePoint actual = ScorePoint.fromString(String.valueOf(expected.typeId));

            assertThat(actual, is(expected));
        }

        @Test
        public void 存在しないIDではNOT_INPUTが返却される() {

            final ScorePoint expetcted = ScorePoint.NotInput;
            final ScorePoint actual = ScorePoint.fromString("999999999");

            assertThat(actual, is(expetcted));
        }

        @Test
        public void NullではNOT_INPUTが返却される() {

            final ScorePoint expetcted = ScorePoint.NotInput;
            final ScorePoint actual = ScorePoint.fromString(null);

            assertThat(actual, is(expetcted));
        }

        @Test
        public void ブランクではNOT_INPUTが返却される() {

            final ScorePoint expetcted = ScorePoint.NotInput;
            final ScorePoint actual = ScorePoint.fromString("");

            assertThat(actual, is(expetcted));
        }
    }

    public static class getSelectableScoresのテスト {

        @Test
        public void 選択可能な配列が返却される() {

            final List<ScorePoint> actualArrays = ScorePoint.getSelectableScores();

            for (ScorePoint actual : actualArrays) {

                assertThat(ScorePoint.NotInput.equals(actual), is(false));
            }
        }
    }
}
