package jp.ne.glory.web.top;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jp.ne.glory.application.genre.GenreSearchStub;
import jp.ne.glory.application.review.ReviewSearchStub;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.test.review.search.ReviewSearchDataGenerator;
import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewView;
import jp.ne.glory.ui.top.TopView;
import jp.ne.glory.web.common.PagePaths;
import jp.ne.glory.web.common.WebPageConst;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class TopTest {

    public static class viewのテスト {

        private Top sut = null;
        private GenreSearchStub genreListStub = null;
        private ReviewSearchStub reviewSearchStub = null;

        @Before
        public void setUp() {

            final List<Genre> testGenreDatas = ReviewSearchDataGenerator.createBaseGenreList();
            final List<ReviewSearchResult> testResultData
                    = ReviewSearchDataGenerator.createBaseSearchResults(10, testGenreDatas);

            reviewSearchStub = new ReviewSearchStub(testResultData);
            genreListStub = new GenreSearchStub(testGenreDatas);
            sut = new Top(reviewSearchStub, genreListStub);
        }

        @Test
        public void トップ画面の表示がされる() {

            final Viewable viewable = sut.view();

            assertThat(viewable.getTemplateName(), is(PagePaths.TOP));

            assertThat(viewable.getModel(), is(instanceOf(TopView.class)));
            final TopView actualView = (TopView) viewable.getModel();

            final ReviewView actualReviews = actualView.getReview();
            assertThat(actualReviews.getReviews().size(), is(WebPageConst.PAGE_PER_REVIEWS));

            final Map<Long, Genre> stubGenres = genreListStub.getAllGenres()
                    .stream()
                    .collect(Collectors.toMap(v -> v.getId().getValue(), v -> v));

            final GenreSearchView actualGenreSearch = actualView.getGenreSearch();
            actualGenreSearch.getGenres()
                    .stream()
                    .forEach(v -> {
                final Genre stubGenre = stubGenres.get(v.getId());

                assertThat(v.getId(), is(stubGenre.getId().getValue()));
                assertThat(v.getName(), is(stubGenre.getName().getValue()));
                    });
        }
    }
}
