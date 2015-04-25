
package jp.ne.glory.web.admin.genre;

import java.net.URI;
import java.util.stream.LongStream;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepositoryStub;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.ui.admin.genre.GenreDetailView;
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
public class GenreDetailTest {

    public static class viewのテスト {

        private GenreDetail sut = null;
        private GenreRepositoryStub stub = null;

        @Before
        public void setUp() {

            LongStream.rangeClosed(0, 10)
                    .mapToObj(v -> new Genre(new GenreId(v), new GenreName("ジャンル" + v)))
                    .forEach(stub::save);

            final GenreSearch genreList = new GenreSearch(stub);

            sut = new GenreDetail(genreList);
        }

        @Test
        public void 指定したジャンルIDに紐付くジャンルが取得できる() {

            final long genreId = 5;

            final Response actualResponse = sut.view(genreId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.GENRE_DETAIL));
            assertThat(viewable.getModel(), is(instanceOf(GenreDetailView.class)));

            final GenreDetailView actualView = (GenreDetailView) viewable.getModel();

            assertThat(actualView.getGenreId(), is(genreId));
            assertThat(actualView.getName(), is("ジャンル5"));
        }

        @Test
        public void 指定したジャンルIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long genreId = Long.MAX_VALUE;
            final Response actualResponse = sut.view(genreId);

            final String base = UriBuilder.fromResource(GenreDetailView.class).toTemplate();
            final String append = UriBuilder.fromMethod(GenreDetailView.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(genreId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }

    public static class notFoundのテスト {

        private GenreDetail sut = null;

        @Before
        public void setUp() {

            sut = new GenreDetail(new GenreSearch(null));
        }

        @Test
        public void エラー画面が表示される() {

            final Viewable viewable = sut.notFound();

            assertThat(viewable.getTemplateName(), is(PagePaths.USER_NOT_FOUND));
            assertThat(viewable.getModel(), is(nullValue()));
        }
    }
}