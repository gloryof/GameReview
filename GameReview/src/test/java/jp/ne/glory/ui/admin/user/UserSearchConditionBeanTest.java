
package jp.ne.glory.ui.admin.user;

import jp.ne.glory.domain.user.value.search.UserSearchCondition;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class UserSearchConditionBeanTest {

    public static class 入力値が未設定の場合 {

        private UserSearchConditionBean sut = null;

        @Before
        public void setUp() {

            sut = new UserSearchConditionBean();
        }

        @Test
        public void createEntityで条件未設定のエンティティが作成される() {

            final UserSearchCondition actualEntity = sut.createEntity();

            assertThat(actualEntity, is(not(nullValue())));

            assertThat(actualEntity.getLoginId().getValue(), is(""));
            assertThat(actualEntity.getUserName().getValue(), is(""));
        }
    }

    public static class 全ての入力値が設定されている場合 {

        private UserSearchConditionBean sut = null;

        @Before
        public void setUp() {

            sut = new UserSearchConditionBean();
            sut.setLoginId("test-user");
            sut.setUserName(("テストユーザ"));
        }

        @Test
        public void createEntityで条件未設定のエンティティが作成される() {

            final UserSearchCondition actualEntity = sut.createEntity();

            assertThat(actualEntity, is(not(nullValue())));

            assertThat(actualEntity.getLoginId().getValue(), is(sut.getLoginId()));
            assertThat(actualEntity.getUserName().getValue(), is(sut.getUserName()));
        }
    }
}