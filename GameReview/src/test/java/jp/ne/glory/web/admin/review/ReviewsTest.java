package jp.ne.glory.web.admin.review;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import jp.ne.glory.application.genre.GenreSearchStub;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.repository.ReviewRepositoryStub;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.test.genre.list.GenreListDataGenerator;
import jp.ne.glory.test.review.search.ReviewSearchDataGenerator;
import jp.ne.glory.ui.admin.review.ReviewBean;
import jp.ne.glory.ui.admin.review.ReviewListView;
import jp.ne.glory.ui.admin.review.ReviewSearchConditionBean;
import jp.ne.glory.ui.admin.review.ScoreBean;
import jp.ne.glory.ui.genre.GenreBean;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class ReviewsTest {

    private static class TestTool {

        private static void assertViewable(final Viewable actual) {

            assertThat(actual.getTemplateName(), is(PagePaths.REVIEW_LIST));
            assertThat(actual.getModel(), is(instanceOf(ReviewListView.class)));
        }

        private static void assertEmptyCondition(final ReviewSearchConditionBean actual) {

            assertThat(actual.getTitle(), is(nullValue()));
            assertThat(actual.getCeroRating(), is(nullValue()));
            assertThat(actual.getGenreId(), is(nullValue()));
            assertThat(actual.getFrom(), is(nullValue()));
            assertThat(actual.getTo(), is(nullValue()));
            assertThat(actual.getPageNumber(), is(nullValue()));
        }

        private static void assertCondition(final ReviewSearchConditionBean actual,
                final ReviewSearchConditionBean expected) {

            assertThat(actual.getTitle(), is(expected.getTitle()));
            assertThat(actual.getCeroRating(), is(expected.getCeroRating()));
            assertThat(actual.getGenreId(), is(expected.getGenreId()));
            assertThat(actual.getFrom(), is(expected.getFrom()));
            assertThat(actual.getTo(), is(expected.getTo()));
            assertThat(actual.getPageNumber(), is(expected.getPageNumber()));
        }

        private static void assertRatigns(final List<CeroRating> actualCeroRatings) {

            final CeroRating[] expcetedRatings = CeroRating.values();
            final int exptectedLength = expcetedRatings.length - 1;

            assertThat(actualCeroRatings.size(), is(exptectedLength));
            for (int i = 0; i < exptectedLength; i++) {

                final CeroRating expectedRating = expcetedRatings[i];
                final CeroRating actualRating = actualCeroRatings.get(i);

                assertThat(actualRating, is(expectedRating));
            }
        }

        private static void assertGenres(final List<GenreBean> actual, final List<Genre> expected) {

            assertThat(actual.size(), is(expected.size()));

            for (int i = 0; i < actual.size(); i++) {

                final GenreBean actualGenre = actual.get(i);
                final Genre expectedGenre = expected.get(i);

                assertThat(actualGenre.getId(), is(expectedGenre.getId().getValue()));
                assertThat(actualGenre.getName(), is(expectedGenre.getName().getValue()));
            }
        }

        private static void assertReview(final ReviewBean actual, final ReviewSearchResult expected) {

            assertThat(actual.getReviewId(), is(expected.getReview().getId().getValue()));
            assertThat(actual.getGoodPoint(), is(expected.getReview().getGoodPoint().getValue()));
            assertThat(actual.getBadPoint(), is(expected.getReview().getBadPoint().getValue()));
            assertThat(actual.getComment(), is(expected.getReview().getComment().getValue()));

            final Score expectedScore = expected.getReview().getScore();
            final ScoreBean actuScore = actual.getScore();
            assertThat(actuScore.getAddiction(), is(expectedScore.getAddiction()));
            assertThat(actuScore.getLoadTime(), is(expectedScore.getLoadTime()));
            assertThat(actuScore.getMusic(), is(expectedScore.getMusic()));
            assertThat(actuScore.getOperability(), is(expectedScore.getOperability()));
            assertThat(actuScore.getStory(), is(expectedScore.getStory()));

            final DateTimeValue actualPostDatetime = actual.getPostDateTime();
            final DateTimeValue expcetedPostDatetime = expected.getReview().getPostTime().getValue();

            assertThat(actualPostDatetime.getValue(), is(expcetedPostDatetime.getValue()));
            assertThat(actual.getGame().getTitle(), is(expected.getGame().getTitle().getValue()));
        }

        private static void assertPager(final ReviewListView actual, final int expectedCount) {

            final PagerInfo actualPager = actual.getPager();
            assertThat(actualPager.getCurrentPage(), is(1));

            final int[] actualPageNumbers = actualPager.getPages();
            assertThat(actualPageNumbers.length, is(expectedCount));

            for (int i = 0; i < expectedCount; i++) {

                assertThat(actualPageNumbers[i], is(i + 1));
            }

        }
    }

    public static class viewのテスト {

        private List<ReviewSearchResult> reviewList = null;
        private List<Genre> expectedGenre = null;

        private Viewable actualViewable = null;
        private ReviewListView actualView = null;
        private ReviewSearchConditionBean actualCondition = null;

        @Before
        public void setUp() {

            expectedGenre = GenreListDataGenerator.createGenreList(5);
            reviewList = ReviewSearchDataGenerator.createBaseSearchResults(200, expectedGenre);

            final ReviewRepositoryStub stub = new ReviewRepositoryStub();
            final GameRepositoryStub gameStub = new GameRepositoryStub();

            reviewList.forEach(v -> {
                stub.save(v.getReview(), v.getGame(), v.getGenre());
                gameStub.save(v.getGame());
            });

            final GenreSearchStub genreStub = new GenreSearchStub(expectedGenre);

            final Reviews sut = new Reviews(new ReviewSearch(stub), genreStub);
            actualViewable = sut.view();
            actualView = (ReviewListView) actualViewable.getModel();
            actualCondition = actualView.getCondition();
        }

        @Test
        public void Viewableには結果一覧の情報が設定される() {

            TestTool.assertViewable(actualViewable);
        }

        @Test
        public void 検索条件は空が設定される() {

            TestTool.assertEmptyCondition(actualCondition);
        }

        @Test
        public void CEROレーティングのリストは全てのレーティングが設定される() {

            TestTool.assertRatigns(actualCondition.getRatings());
        }

        @Test
        public void ジャンルのリストは全てのジャンルが設定される() {

            TestTool.assertGenres(actualCondition.getGenres(), expectedGenre);
        }

        @Test
        public void リストは全件表示される() {

            final List<ReviewBean> actualReviews = actualView.getReviews();
            assertThat(actualReviews.size(), is(20));

            IntStream.range(0, actualReviews.size()).forEach(i -> {
                final ReviewBean actualReview = actualReviews.get(i);
                final ReviewSearchResult expectedReview = reviewList.get(i);

                TestTool.assertReview(actualReview, expectedReview);
            });

            TestTool.assertPager(actualView, 10);
        }
    }

    @RunWith(Enclosed.class)
    public static class searchのテスト {

        public static class 検索条件を全て入力した場合 {
            private List<ReviewSearchResult> reviewList = null;
            private List<Genre> expectedGenre = null;

            private Viewable actualViewable = null;
            private ReviewListView actualView = null;
            private ReviewSearchConditionBean expectedCondition = null;
            private ReviewSearchConditionBean actualCondition = null;

            @Before
            public void setUp() {

                expectedGenre = GenreListDataGenerator.createGenreList(5);
                reviewList = ReviewSearchDataGenerator.createBaseSearchResults(200, expectedGenre);

                final ReviewRepositoryStub stub = new ReviewRepositoryStub();
                final GameRepositoryStub gameStub = new GameRepositoryStub();

                reviewList.forEach(v -> {
                    stub.save(v.getReview(), v.getGame(), v.getGenre());
                    gameStub.save(v.getGame());
                });

                final GenreSearchStub genreStub = new GenreSearchStub(expectedGenre);
                expectedCondition = new ReviewSearchConditionBean(expectedGenre);
                expectedCondition.setCeroRating(CeroRating.A);
                expectedCondition.setGenreId(2l);
                expectedCondition.setPageNumber(3);
                expectedCondition.setTitle("タイトル");

                expectedCondition.setFrom(LocalDate.MIN);
                expectedCondition.setTo(LocalDate.MAX);

                final Reviews sut = new Reviews(new ReviewSearch(stub), genreStub);
                actualViewable = sut.search(expectedCondition);
                actualView = (ReviewListView) actualViewable.getModel();
                actualCondition = actualView.getCondition();
            }

            @Test
            public void Viewableには結果一覧の情報が設定される() {

                TestTool.assertViewable(actualViewable);
            }

            @Test
            public void 入力した検索条件がそのまま設定される() {

                TestTool.assertCondition(actualCondition, expectedCondition);
            }

            @Test
            public void CEROレーティングのリストは全てのレーティングが設定される() {

                TestTool.assertRatigns(actualCondition.getRatings());
            }

            @Test
            public void ジャンルのリストは全てのジャンルが設定される() {

                TestTool.assertGenres(actualCondition.getGenres(), expectedGenre);
            }

            @Test
            public void リストに検索結果が設定される() {

                // 検索結果が正しい事については内部のクラスの範囲のためテストはしない
                final List<ReviewBean> actualReviews = actualView.getReviews();
                assertFalse(actualReviews.isEmpty());
            }

        }
    }
}
