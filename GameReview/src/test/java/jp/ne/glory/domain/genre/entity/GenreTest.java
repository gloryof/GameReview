package jp.ne.glory.domain.genre.entity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class GenreTest {
    
    public static class 全ての値が正常に設定されている場合 {

        private Genre sut = null;

        private final GenreId INIT_ID = new GenreId(123L);
        private final GenreName INIT_NAME = new GenreName("アクション");

        @Before
        public void setUp() {

            sut = new Genre(INIT_ID, INIT_NAME);
        }

        @Test
        public void コンストラクタで設定した値がプロパティに設定されている() {

            assertThat(sut.id.isSame(INIT_ID), is(true));
            assertThat(sut.name.value, is(INIT_NAME.value));
        }
        
        @Test
        public void isRegisteredにtrueが設定されている() {

            assertThat(sut.isRegistered(), is(true));
        }
    }

    public static class コンストラクタに初期値が設定されている場合 {

        private Genre sut = null;

        private final GenreId INIT_ID = GenreId.notNumberingValue();
        private final GenreName INIT_NAME = GenreName.empty();

        @Before
        public void setUp() {

            sut = new Genre(INIT_ID, INIT_NAME);
        }

        @Test
        public void 全てに初期値が設定されている() {

            assertThat(sut.id.isSame(INIT_ID), is(true));
            assertThat(sut.name.value, is(INIT_NAME.value));
        }

        @Test
        public void isRegisteredにfalseが設定されている() {

            assertThat(sut.isRegistered(), is(false));
        }
    }
}
