package jp.ne.glory.domain.review.value;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReviewIdTest {
    
    public static class notNumberingValueのテスト {

        @Test
        public void 値が0で未採番の値が返却される() {

            final ReviewId actual  = ReviewId.notNumberingValue();

            assertThat(actual.getValue(), is(0L));
            assertThat(actual.isSetValue(), is(false));
        }
    }
}
