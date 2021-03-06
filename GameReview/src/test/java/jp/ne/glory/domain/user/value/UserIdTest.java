/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.glory.domain.user.value;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class UserIdTest {
    public static class notNumberingValueのテスト {

        @Test
        public void 値が0で未採番の値が返却される() {

            final UserId actual  = UserId.notNumberingValue();

            assertThat(actual.getValue(), is(0L));
            assertThat(actual.isSetValue(), is(false));
        }
    }
}
