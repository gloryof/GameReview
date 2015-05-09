
package jp.ne.glory.web.admin.genre;

import java.net.URI;
import java.util.stream.LongStream;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.genre.GenreRegister;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.ui.admin.genre.GenreEditView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GenreEditTest {

    public static class viewのテスト {

        private GenreEdit sut = null;
        private GenreRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new GenreRepositoryStub();
            LongStream.range(0, 10)
                    .mapToObj(v -> new Genre(new GenreId(v), new GenreName("ジャンル" + v)))
                    .forEach(stub::save);

            sut = new GenreEdit(new GenreRegister(stub), new GenreSearch(stub));
        }

        @Test
        public void 指定したジャンルIDに紐付くジャンルが取得できる() {

            final Response actualResponse = sut.view(5);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.GENRE_EDIT));
            assertThat(viewable.getModel(), is(instanceOf(GenreEditView.class)));

            final GenreEditView actualView = (GenreEditView) viewable.getModel();

            assertThat(actualView.getGenreId(), is(5L));
            assertThat(actualView.getName(), is("ジャンル5"));
        }

        @Test
        public void 指定したジャンルIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long genreId = Long.MAX_VALUE;
            final Response actualResponse = sut.view(genreId);

            final String base = UriBuilder.fromResource(GenreDetail.class).toTemplate();
            final String append = UriBuilder.fromMethod(GenreDetail.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(genreId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }

    public static class completeEditのテスト {

        private GenreEdit sut = null;
        private GenreRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new GenreRepositoryStub();
            LongStream.range(0, 10)
                    .mapToObj(v -> new Genre(new GenreId(v), new GenreName("ジャンル" + v)))
                    .forEach(stub::save);

            sut = new GenreEdit(new GenreRegister(stub), new GenreSearch(stub));
        }

        @Test
        public void 正常な値が入力されている場合_保存されて_詳細画面を表示する() {

            final long paramGenreId = 5L;
            final GenreEditView expectedView = new GenreEditView();
            expectedView.setGenreId(paramGenreId);
            expectedView.setName("変更後ジャンル");

            final Response actualResponse = sut.completeEdit(paramGenreId, expectedView);
            final String templatePath = UriBuilder.fromResource(GenreDetail.class).toTemplate();
            final URI uri = UriBuilder.fromUri(templatePath).resolveTemplate("genreId", paramGenreId).build();

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(uri));

            final Genre actualGenre = stub.findBy(new GenreId(paramGenreId)).get();

            assertThat(actualGenre.getId().getValue(), is(expectedView.getGenreId()));
            assertThat(actualGenre.getName().getValue(), is(expectedView.getName()));
        }

        @Test
        public void ドメイン入力チェックエラーの場合_編集画面を表示する() {

            final long paramGenreId = 5L;
            final GenreEditView expectedView = new GenreEditView();
            expectedView.setGenreId(paramGenreId);
            expectedView.setName("");

            final Response actualResponse = sut.completeEdit(paramGenreId, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.GENRE_EDIT));
            assertThat(viewable.getModel(), is(instanceOf(GenreEditView.class)));

            final GenreEditView actualView = (GenreEditView) viewable.getModel();

            assertThat(actualView.getGenreId(), is(5L));
            assertThat(actualView.getName(), is(""));
        }

        @Test
        public void URLのジャンルIDと入力値のジャンルIDがとなる場合_Badリクエストエラーになる() {

            final long paramGenreId = 5L;
            final GenreEditView expectedView = new GenreEditView();
            expectedView.setGenreId(paramGenreId);

            final Response actualResponse = sut.completeEdit(10l, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 入力値のジャンルIDが未設定の場合_Badリクエストエラーになる() {

            final long paramUserId = 5L;
            final GenreEditView expectedView = new GenreEditView();

            final Response actualResponse = sut.completeEdit(paramUserId, expectedView);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 指定したジャンルIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long paramGenreId = 100L;
            final GenreEditView expectedView = new GenreEditView();
            expectedView.setGenreId(paramGenreId);

            final Response actualResponse = sut.completeEdit(paramGenreId, expectedView);

            final String base = UriBuilder.fromResource(GenreDetail.class).toTemplate();
            final String append = UriBuilder.fromMethod(GenreDetail.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(paramGenreId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }
}