package jp.ne.glory.domain.user.value;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class AuthorityTest {

    public static class getByIdのテスト {

        @Test
        public void 存在するIDで取得できる_パターン1() {

            final Authority expetcted = Authority.ConfigChange;
            final Authority actual = Authority.getByAuthorityId(expetcted.authorityId);

            assertThat(actual, is(expetcted));
        }

        @Test
        public void 存在するIDで取得できる_パターン2() {

            final Authority expetcted = Authority.ReviewPost;
            final Authority actual = Authority.getByAuthorityId(expetcted.authorityId);

            assertThat(actual, is(expetcted));
        }

        @Test
        public void 存在しないIDでNONEが返却される() {

            final Authority expetcted = Authority.None;
            final Authority actual = Authority.getByAuthorityId(-123456);

            assertThat(actual, is(expetcted));
        }
    }
}
