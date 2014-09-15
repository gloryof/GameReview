/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.glory.common.annotation.param;

import jp.ne.glory.domain.common.annotation.param.ValidCharcterType;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 *
 * @author Admin
 */
@RunWith(Enclosed.class)
public class ValidCharcterTypeTest {

    public static class OnlySingleByteCharsのテスト {

        @Test
        public void 半角文字のみの場合isMatcheでtrueが返却される() {

            final ValidCharcterType sut = ValidCharcterType.OnlySingleByteChars;
            final  StringBuilder builder = new StringBuilder();

            assertThat(sut.isMatch("0123456789"), is(true));
            assertThat(sut.isMatch("abcdefghijklmnopqrstuvwxyz"), is(true));
            assertThat(sut.isMatch("ABCDEFGHIJKLMNOPQRSTUVWXYZ"), is(true));
            assertThat(sut.isMatch("!\"#$%&'()=~|+*}<>?_,./\\"), is(true));
        }

        @Test
        public void 半角文字以外の場合isMatcheでtrueが返却される() {

            final ValidCharcterType sut = ValidCharcterType.OnlySingleByteChars;
            final  StringBuilder builder = new StringBuilder();
            assertThat(sut.isMatch("１"), is(false));
        }
    }
}
