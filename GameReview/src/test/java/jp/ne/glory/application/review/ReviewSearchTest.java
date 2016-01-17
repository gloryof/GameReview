package jp.ne.glory.application.review;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.common.type.DateValue;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.review.repository.ReviewRepositoryStub;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchOrderType;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;
import jp.ne.glory.test.review.search.ReviewSearchDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class ReviewSearchTest {

    public static class searchNewRevewsのテスト {

        private ReviewSearch sut = null;
        private ReviewRepositoryStub repoStub = null;

        @Before
        public void setUp() {

            repoStub = new ReviewRepositoryStub();
            final List<ReviewSearchResult> searchList = ReviewSearchDataGenerator.createBaseSearchResults(10);

            searchList
                    .stream()
                    .forEach(v -> repoStub.addResult(v));
            sut = new ReviewSearch(repoStub);
        }

        @Test
        public void 検索結果が返却される() {

            final int expectedAllCount = 10;
            final int expectedPagePerCount = 5;
            final int expectedLotNumber = 1;

            final ReviewSearchResults actualResult = sut.searchNewReviews(expectedPagePerCount, expectedLotNumber);

            final ReviewSearchCondition actualCondition = actualResult.getCondition();
            final List<ReviewSearchResult> actualList = actualResult.getResults();

            assertThat(actualCondition, is(not(nullValue())));
            assertThat(actualCondition.getTargetCount(), is(expectedPagePerCount));
            assertThat(actualCondition.getLotPerCount(), is(expectedPagePerCount));
            assertThat(actualCondition.getLotNumber(), is(expectedLotNumber));
            assertThat(actualCondition.getOrderType(), is(ReviewSearchOrderType.PostTimeDesc));

            assertThat(actualList.size(), is(expectedPagePerCount));

            assertThat(actualResult.getAllCount(), is(expectedAllCount));
            assertThat(actualResult.hasNextLot(), is(true));
            assertThat(actualResult.hasPrevLot(), is(false));
        }
    }

    public static class searchReviewByGenreのテスト {

        private ReviewSearch sut = null;
        private ReviewRepositoryStub repoStub = null;

        @Before
        public void setUp() {

            repoStub = new ReviewRepositoryStub();
            final List<ReviewSearchResult> searchList = ReviewSearchDataGenerator.createBaseSearchResults(30);

            searchList
                    .stream()
                    .forEach(v -> repoStub.addResult(v));

            sut = new ReviewSearch(repoStub);
        }

        @Test
        public void 指定したジャンルのみが取得される() {

            final int expectedAllCount = 10;
            final int expectedPagePerCount = 5;
            final int expectedLotNumber = 1;

            final GenreId genreId = new GenreId(2L);
            final ReviewSearchResults actualResult = sut.searchReviewByGenre(genreId, expectedPagePerCount, expectedLotNumber);

            final ReviewSearchCondition actualCondition = actualResult.getCondition();
            final List<ReviewSearchResult> actualList = actualResult.getResults();

            actualList.forEach(v -> assertThat(v.getGenre().getId().getValue().equals(genreId.getValue()), is(true)));

            assertThat(actualCondition, is(not(nullValue())));
            assertThat(actualCondition.getTargetCount(), is(0));
            assertThat(actualCondition.getLotPerCount(), is(expectedPagePerCount));
            assertThat(actualCondition.getLotNumber(), is(expectedLotNumber));
            assertThat(actualCondition.getOrderType(), is(ReviewSearchOrderType.PostTimeDesc));

            assertThat(actualList.size(), is(expectedPagePerCount));

            assertThat(actualResult.getAllCount(), is(expectedAllCount));
            assertThat(actualResult.hasNextLot(), is(true));
            assertThat(actualResult.hasPrevLot(), is(false));
        }
    }

    public static class searchByReviewIdのテスト {

        private ReviewSearch sut = null;
        private ReviewRepositoryStub repoStub = null;

        @Before
        public void setUp() {

            repoStub = new ReviewRepositoryStub();
            final List<ReviewSearchResult> searchList = ReviewSearchDataGenerator.createBaseSearchResults(30);

            searchList
                    .stream()
                    .forEach(v -> repoStub.addResult(v));

            sut = new ReviewSearch(repoStub);
        }

        @Test
        public void 指定したレビューIDのレビュー情報が取得できる() {

            final ReviewId expectedReviewId = new ReviewId(1l);
            final ReviewSearchResult actualResult = sut.searchByReviewId(expectedReviewId).get();

            assertThat(expectedReviewId.getValue(), is(actualResult.getReview().getId().getValue()));
        }
    }

    @RunWith(Enclosed.class)
    public static class searchのテスト {

        public static class 結果がある場合 {

            private ReviewSearchResults actualResults = null;
            private List<Long> expectedReviewIds = null;

            @Before
            public void setUp() {

                final ReviewRepositoryStub repoStub = new ReviewRepositoryStub();
                List<ReviewSearchResult> searchList = ReviewSearchDataGenerator.createBaseSearchResults(30,
                        LocalDateTime.of(2015, 11, 1, 0, 0));

                searchList
                        .stream()
                        .forEach(v -> repoStub.addResult(v));

                final ReviewSearch sut = new ReviewSearch(repoStub);

                expectedReviewIds = new ArrayList<>();
                expectedReviewIds.add(1l);
                expectedReviewIds.add(3l);
                expectedReviewIds.add(5l);

                final ReviewSearchCondition condition = new ReviewSearchCondition();
                expectedReviewIds.forEach(v -> condition.addReviewId(new ReviewId(v)));

                actualResults = sut.search(condition);
            }

            @Test
            public void 検索結果で指定した結果と件数が返却される() {

                assertThat(actualResults.getAllCount(), is(3));

                final List<ReviewSearchResult> actualList = actualResults.getResults();
                assertThat(actualList.size(), is(3));
                actualList.stream()
                        .map(v -> v.getReview().getId().getValue())
                        .forEach(v -> assertTrue(expectedReviewIds.contains(v)));
            }
        }

        public static class 検索結果がない場合 {

            private ReviewSearchResults actualResults = null;

            @Before
            public void setUp() {

                final ReviewRepositoryStub repoStub = new ReviewRepositoryStub();
                List<ReviewSearchResult> searchList = ReviewSearchDataGenerator.createBaseSearchResults(30,
                        LocalDateTime.of(2015, 11, 1, 0, 0));

                searchList
                        .stream()
                        .forEach(v -> repoStub.addResult(v));

                final ReviewSearch sut = new ReviewSearch(repoStub);

                final ReviewSearchCondition condition = new ReviewSearchCondition();
                condition.addReviewId(new ReviewId(Long.MIN_VALUE));
                actualResults = sut.search(condition);
            }

            @Test
            public void 件数は0件() {

                assertThat(actualResults.getAllCount(), is(0));
            }

            @Test
            public void 結果は空のリスト() {

                final List<ReviewSearchResult> actualList = actualResults.getResults();
                assertTrue(actualList.isEmpty());
            }
        }

        public static class 検索条件に全ての値が入力されている場合 {

            private ReviewSearchCondition actualCondition = null;
            private ReviewSearchCondition expectedCondition = null;

            @Before
            public void setUp() {

                final ReviewRepositoryStub repoStub = new ReviewRepositoryStub();
                List<ReviewSearchResult> searchList = ReviewSearchDataGenerator.createBaseSearchResults(30,
                        LocalDateTime.of(2015, 11, 1, 0, 0));

                searchList
                        .stream()
                        .forEach(v -> repoStub.addResult(v));

                final ReviewSearch sut = new ReviewSearch(repoStub);

                actualCondition = createTestDataCondition();
                ReviewSearchResults actualResults = sut.search(actualCondition);

                expectedCondition = actualResults.getCondition();
            }

            @Test
            public void 入力したレビューIDが保持されている() {

                final List<ReviewId> actualReviewIds = actualCondition.getReviewIds();
                final List<ReviewId> expectedReviewIds = expectedCondition.getReviewIds();
                assertThat(actualReviewIds.size(), is(expectedReviewIds.size()));
                for (int i = 0; i < actualReviewIds.size(); i++) {

                    final ReviewId actualReviewId = actualReviewIds.get(i);
                    final ReviewId expectedReviewId = expectedReviewIds.get(i);
                    assertThat(actualReviewId.getValue(), is(expectedReviewId.getValue()));
                }
            }

            @Test
            public void 入力したジャンルIDが保持されている() {

                final List<GenreId> actualGenreIds = actualCondition.getGenreIds();
                final List<GenreId> expectedGenreIds = expectedCondition.getGenreIds();
                assertThat(actualGenreIds.size(), is(expectedGenreIds.size()));
                for (int i = 0; i < actualGenreIds.size(); i++) {

                    final GenreId actualGenreId = actualGenreIds.get(i);
                    final GenreId expectedGenreId = expectedGenreIds.get(i);
                    assertThat(actualGenreId.getValue(), is(expectedGenreId.getValue()));
                }
            }

            @Test
            public void 入力したCEROレーティングが保持されている() {

                final List<CeroRating> actualCeroRatings = actualCondition.getCeroRatigns();
                final List<CeroRating> expectedCeroRatings = expectedCondition.getCeroRatigns();
                assertThat(actualCeroRatings.size(), is(expectedCeroRatings.size()));
                for (int i = 0; i < actualCeroRatings.size(); i++) {

                    final CeroRating actualCeroRating = actualCeroRatings.get(i);
                    final CeroRating expectedCeroRating = expectedCeroRatings.get(i);
                    assertThat(actualCeroRating, is(expectedCeroRating));
                }
            }

            @Test
            public void 入力したタイトルが保持されている() {

                assertThat(actualCondition.getTitle(), is(expectedCondition.getTitle()));
            }

            @Test
            public void 入力したFromが保持されている() {

                final DateValue actualFrom = actualCondition.getPostedFrom();
                final DateValue exptedFrom = expectedCondition.getPostedFrom();

                assertThat(actualFrom.getValue(), is(exptedFrom.getValue()));
            }

            @Test
            public void 入力したToが保持されている() {

                final DateValue actualTo = actualCondition.getPostedTo();
                final DateValue exptedTo = expectedCondition.getPostedTo();

                assertThat(actualTo.getValue(), is(exptedTo.getValue()));
            }

            @Test
            public void 入力したページャ情報が保持されている() {

                assertThat(actualCondition.getLotNumber(), is(expectedCondition.getLotNumber()));
                assertThat(actualCondition.getLotPerCount(), is(expectedCondition.getLotPerCount()));
                assertThat(actualCondition.getTargetCount(), is(expectedCondition.getTargetCount()));
            }

            private ReviewSearchCondition createTestDataCondition() {

                final ReviewSearchCondition condition = new ReviewSearchCondition();

                condition.addReviewId(new ReviewId(1000l));
                condition.addReviewId(new ReviewId(1001l));
                condition.addReviewId(new ReviewId(1002l));

                condition.addGenreId(new GenreId(2000l));
                condition.addGenreId(new GenreId(2001l));
                condition.addGenreId(new GenreId(2002l));

                condition.addCeroRating(CeroRating.A);
                condition.addCeroRating(CeroRating.C);
                condition.addCeroRating(CeroRating.Z);

                condition.setTitle("タイトル");

                condition.setPostedFrom(new DateValue(LocalDate.of(2016, 1, 2)));
                condition.setPostedTo(new DateValue(LocalDate.of(2016, 3, 4)));

                condition.setLotNumber(3);
                condition.setLotPerCount(4);
                condition.setTargetCount(5);

                return condition;
            }
        }
    }
}
