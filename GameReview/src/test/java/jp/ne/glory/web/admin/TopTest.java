
package jp.ne.glory.web.admin;

import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class TopTest {

    public static class viewのテスト {

        private Top sut = null;

        @Before
        public void setUp() {

            sut = new Top();
        }

        @Test
        public void 管理画面TOPが表示される() {

            final Viewable viewable = sut.view();

            assertThat(viewable.getTemplateName(), is(PagePaths.ADMIN_TOP));
        }
    }
}