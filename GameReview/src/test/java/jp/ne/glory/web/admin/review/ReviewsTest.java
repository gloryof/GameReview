package jp.ne.glory.web.admin.review;

import java.util.List;
import java.util.stream.IntStream;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.review.repository.ReviewRepositoryStub;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.test.review.search.ReviewSearchDataGenerator;
import jp.ne.glory.ui.admin.game.GameSearchConditionBean;
import jp.ne.glory.ui.admin.review.DateRange;
import jp.ne.glory.ui.admin.review.ReviewBean;
import jp.ne.glory.ui.admin.review.ReviewListView;
import jp.ne.glory.ui.admin.review.ReviewSearchConditionBean;
import jp.ne.glory.web.common.PagePaths;
import jp.ne.glory.web.common.PagerInfo;
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
public class ReviewsTest {

    private static class TestTool {

        private static void assertNotUsedCondition(final GameSearchConditionBean actualCondition) {

            assertThat(actualCondition.getCeroRating(), is(nullValue()));
            assertThat(actualCondition.getGenreId(), is(nullValue()));
            assertThat(actualCondition.getGenres(), is(nullValue()));
            assertThat(actualCondition.getPageNumber(), is(nullValue()));

            final List<CeroRating> actualCeroRatings = actualCondition.getRatings();
            final CeroRating[] expcetedRatings = CeroRating.values();
            final int exptectedLength = expcetedRatings.length - 1;

            assertThat(actualCeroRatings.size(), is(exptectedLength));
            for (int i = 0; i < exptectedLength; i++) {

                final CeroRating expectedRating = expcetedRatings[i];
                final CeroRating actualRating = actualCeroRatings.get(i);

                assertThat(actualRating, is(expectedRating));
            }

        }
    }

    public static class viewのテスト {

        private Reviews sut = null;
        private List<ReviewSearchResult> reviewList = null;

        @Before
        public void setUp() {

            reviewList = ReviewSearchDataGenerator.createBaseSearchResults(200);

            final ReviewRepositoryStub stub = new ReviewRepositoryStub();
            final GameRepositoryStub gameStub = new GameRepositoryStub();

            reviewList.forEach(v -> {
                stub.save(v.getReview(), v.getGame(), v.getGenre());
                gameStub.save(v.getGame());
            });

            sut = new Reviews(new ReviewSearch(stub));
        }

        @Test
        public void 検索条件は未入力_リストは全件表示される() {

            final Viewable viewable = sut.view();

            assertThat(viewable.getTemplateName(), is(PagePaths.REVIEW_LIST));
            assertThat(viewable.getModel(), is(instanceOf(ReviewListView.class)));

            final ReviewListView actualView = (ReviewListView) viewable.getModel();

            final ReviewSearchConditionBean actualCondition = actualView.getCondition();

            final DateRange actualRange = actualCondition.getRange();
            assertThat(actualRange.getFrom(), is(nullValue()));
            assertThat(actualRange.getTo(), is(nullValue()));

            final GameSearchConditionBean actualGameCondition = actualCondition.getGameCondition();
            assertThat(actualGameCondition.getTitle(), is(nullValue()));
            TestTool.assertNotUsedCondition(actualGameCondition);

            final List<ReviewBean> actualReviews = actualView.getReviews();
            assertThat(actualReviews.size(), is(20));

            IntStream.range(0, actualReviews.size()).forEach(i -> {
                final ReviewBean actualReview = actualReviews.get(i);
                final ReviewSearchResult expectedReview = reviewList.get(i);

                assertThat(actualReview.getReviewId(), is(expectedReview.getReview().getId().getValue()));
                assertThat(actualReview.getGoodPoint(), is(expectedReview.getReview().getGoodPoint().getValue()));
                assertThat(actualReview.getBadPoint(), is(expectedReview.getReview().getBadPoint().getValue()));
                assertThat(actualReview.getComment(), is(expectedReview.getReview().getComment().getValue()));
                assertThat(actualReview.getAddiction(), is(expectedReview.getReview().getScore().getAddiction()));
                assertThat(actualReview.getLoadTime(), is(expectedReview.getReview().getScore().getLoadTime()));
                assertThat(actualReview.getMusic(), is(expectedReview.getReview().getScore().getMusic()));
                assertThat(actualReview.getOperability(), is(expectedReview.getReview().getScore().getOperability()));
                assertThat(actualReview.getStory(), is(expectedReview.getReview().getScore().getStory()));

                final DateTimeValue actualPostDatetime = actualReview.getPostDatetime();
                final DateTimeValue expcetedPostDatetime = expectedReview.getReview().getPostTime().getValue();

                assertThat(actualPostDatetime.getValue(), is(expcetedPostDatetime.getValue()));
                assertThat(actualReview.getGame().getTitle(), is(expectedReview.getGame().getTitle().getValue()));
            });

            final PagerInfo actualPager = actualView.getPager();
            assertThat(actualPager.getCurrentPage(), is(1));

            final int[] actualPageNumbers = actualPager.getPages();
            assertThat(actualPageNumbers.length, is(10));

            for (int i = 0; i < 10; i++) {

                assertThat(actualPageNumbers[i], is(i + 1));
            }

        }
    }
}
