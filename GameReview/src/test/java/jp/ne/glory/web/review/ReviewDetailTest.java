
package jp.ne.glory.web.review;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jp.ne.glory.application.genre.GenreSearchStub;
import jp.ne.glory.application.review.ReviewSearchStub;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;
import jp.ne.glory.test.review.search.ReviewSearchDataGenerator;
import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewBean;
import jp.ne.glory.ui.review.ReviewView;
import jp.ne.glory.ui.top.TopView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class ReviewDetailTest {

    private static class TestTool {

        private static void assertTextArea(final List<String> actual, final String exepected) {

            assertThat(actual, is(not(nullValue())));

            final String line = System.getProperty("line.separator");
            final List<String> expectedStrings = Arrays.asList(exepected.split(line));

            assertThat(actual.size(), is(expectedStrings.size()));
            IntStream.range(0, actual.size()).forEach(i -> {
                assertThat(actual.get(i), is(expectedStrings.get(i)));
            });
        }
    }

    public static class viewのテスト {

        private ReviewDetail sut = null;
        private GenreSearchStub genreSearchStub = null;
        private ReviewSearchStub reviewSearchStub = null;

        @Before
        public void setUp() {
            final List<Genre> testGenreDatas = ReviewSearchDataGenerator.createBaseGenreList();
            final List<ReviewSearchResult> testResultData
                    = ReviewSearchDataGenerator.createBaseSearchResults(10, testGenreDatas);

            reviewSearchStub = new ReviewSearchStub(testResultData);
            genreSearchStub = new GenreSearchStub(testGenreDatas);

            sut = new ReviewDetail(reviewSearchStub, genreSearchStub);
        }

        @Test
        public void viewのテスト() {

            final ReviewSearchResults results = reviewSearchStub.searchNewReviews(1, 1);
            final ReviewSearchResult expectedData = results.getResults().get(0);
            final Review expectedReview = expectedData.getReview();
            final Game expectedGame = expectedData.getGame();
            final Genre expectedGenre = expectedData.getGenre();
            final Score expectedScore = expectedReview.getScore();

            final Viewable viewable = sut.view(expectedReview.getId().getValue());

            assertThat(viewable.getTemplateName(), is(PagePaths.TOP));

            assertThat(viewable.getModel(), is(instanceOf(TopView.class)));
            final TopView actualView = (TopView) viewable.getModel();

            final ReviewView actualReviews = actualView.getReview();
            assertThat(actualReviews.getReviews().size(), is(1));

            final ReviewBean actualReview = actualReviews.getReviews().get(0);
            assertThat(actualReview.getReviewId(), is(expectedReview.getId().getValue()));
            assertThat(actualReview.getTitle(), is(expectedGame.getTitle().getValue()));
            assertThat(actualReview.getGenreName(), is(expectedGenre.getName().getValue()));
            assertThat(actualReview.getAddictionScore(), is(expectedScore.getAddiction()));
            assertThat(actualReview.getStoryScore(), is(expectedScore.getStory()));
            assertThat(actualReview.getOperabilityScore(), is(expectedScore.getOperability()));
            assertThat(actualReview.getLoadTimeScore(), is(expectedScore.getLoadTime()));
            assertThat(actualReview.getMusicScore(), is(expectedScore.getMusic()));
            TestTool.assertTextArea(actualReview.getGoodPoint(), expectedReview.getGoodPoint().getValue());
            TestTool.assertTextArea(actualReview.getBadPoint(), expectedReview.getBadPoint().getValue());
            TestTool.assertTextArea(actualReview.getComment(), expectedReview.getComment().getValue());

            final Map<Long, Genre> stubGenres = genreSearchStub.getAllGenres()
                    .stream()
                    .collect(Collectors.toMap(v -> v.getId().getValue(), v -> v));

            final GenreSearchView actualGenreSearch = actualView.getGenreSearch();
            actualGenreSearch.getGenres().stream()
                .forEach(v -> {
                final Genre stubGenre = stubGenres.get(v.getId());

                assertThat(v.getId(), is(stubGenre.getId().getValue()));
                assertThat(v.getName(), is(stubGenre.getName().getValue()));
                });
        }
    }
}