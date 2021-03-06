package jp.ne.glory.web.genreSearch;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jp.ne.glory.application.genre.GenreSearchStub;
import jp.ne.glory.application.review.ReviewSearchStub;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.test.review.search.ReviewSearchDataGenerator;
import jp.ne.glory.ui.genre.GenreSearchResultView;
import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewBean;
import jp.ne.glory.ui.review.ReviewListView;
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
public class GenreSearchTest {

    public static class viewのテスト {

        private GenreList sut = null;
        private GenreSearchStub genreListStub = null;
        private ReviewSearchStub reviewSearchStub = null;

        @Before
        public void setUp() {

            final List<Genre> testGenreDatas = ReviewSearchDataGenerator.createBaseGenreList();
            final List<ReviewSearchResult> testResultData
                    = ReviewSearchDataGenerator.createBaseSearchResults(100, testGenreDatas);

            reviewSearchStub = new ReviewSearchStub(testResultData);
            genreListStub = new GenreSearchStub(testGenreDatas);
            sut = new GenreList(reviewSearchStub, genreListStub);
        }

        @Test
        public void ジャンル検索結果画面が表示される() {

            final Genre expectedGenre = genreListStub.getAllGenres().get(1);
            final Viewable viewable = sut.view(expectedGenre.getId().getValue());

            assertThat(viewable.getTemplateName(), is(PagePaths.GENRE_SEARCH_RESULT));

            assertThat(viewable.getModel(), is(instanceOf(GenreSearchResultView.class)));
            final GenreSearchResultView actualView = (GenreSearchResultView) viewable.getModel();

            final ReviewListView actualReviewList = actualView.getReviewList();
            final List<ReviewBean> actualReviews = actualReviewList.getReviews();
            assertThat(actualReviews.size(), is(WebPageConst.PAGE_PER_SEARCH_REVIEWS));
            actualReviews
                    .stream()
                    .forEach(v -> assertThat(v.getGenreName(), is(expectedGenre.getName().getValue())));

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