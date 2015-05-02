
package jp.ne.glory.web.admin.genre;

import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.genre.GenreRegister;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.test.util.TestUtil;
import jp.ne.glory.ui.admin.genre.GenreEditView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class GenreCreateTest {

    public static class viewのテスト {

        private GenreCreate sut = null;

        @Before
        public void setUp() {

            sut = new GenreCreate();
        }

        @Test
        public void 新規作成画面が表示される() {

            final Viewable actualViewable = sut.view();

            assertThat(actualViewable.getTemplateName(), is(PagePaths.GENRE_CREATE));
            assertThat(actualViewable.getModel(), is(instanceOf(GenreEditView.class)));

            final GenreEditView actualView = (GenreEditView) actualViewable.getModel();

            assertThat(actualView.getGenreId(), is(nullValue()));
            assertThat(actualView.getName(), is(nullValue()));

        }
    }

    public static class createのテスト {

        private GenreCreate sut = null;
        private GenreRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new GenreRepositoryStub();
            final GenreRegister register = new GenreRegister(stub);
            sut = new GenreCreate(register);
        }

        @Test
        public void 正常に登録できた場合_詳細画面を表示する() {

            final GenreEditView paramView = new GenreEditView();
            paramView.setName("ジャンル");

            final long excepctedGenreIdValue = stub.getCurrentSequence();

            final Response actualResponse = sut.create(paramView);

            final URI expectedUri = UriBuilder.fromResource(GenreDetail.class).resolveTemplate("genreId", excepctedGenreIdValue).build();
            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(expectedUri));

            final Genre actualGenre = stub.findBy(new GenreId(excepctedGenreIdValue)).get();
            assertThat(actualGenre.getId().getValue(), is(excepctedGenreIdValue));
            assertThat(actualGenre.getName().getValue(), is(paramView.getName()));
        }

        @Test
        public void 入力エラーがある場合_作成画面を表示する() {

            final GenreEditView expectedView = new GenreEditView();
            expectedView.setName(TestUtil.repeat("あ", 51));

            final Response actualResponse = sut.create(expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.GENRE_CREATE));
            assertThat(viewable.getModel(), is(instanceOf(GenreEditView.class)));

            final GenreEditView actualView = (GenreEditView) viewable.getModel();

            assertThat(actualView.getErrors().hasError(), is(true));
            assertThat(actualView.getGenreId(), is(nullValue()));
            assertThat(actualView.getName(), is(expectedView.getName()));
        }
    }

}