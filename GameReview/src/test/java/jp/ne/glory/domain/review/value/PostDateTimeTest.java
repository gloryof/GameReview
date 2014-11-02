package jp.ne.glory.domain.review.value;

import jp.ne.glory.common.type.DateTimeValue;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class PostDateTimeTest {

    public static class Emptyのテスト {

        @Test
        public void 空のDateTimeValueオブジェクトが返却される() {

            final PostDateTime actual = PostDateTime.empty();
            final DateTimeValue actualDateTime = actual.getValue();

            assertThat(actualDateTime.getValue(), is(nullValue()));
        }
    }
}
