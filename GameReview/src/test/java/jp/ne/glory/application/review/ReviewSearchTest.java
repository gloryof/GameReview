package jp.ne.glory.application.review;

import java.util.List;
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
        public void 指定したジャンルIDのレビュー情報が取得できる() {

            final int expectedAllCount = 1;
            final int expectedPagePerCount = 1;
            final int expectedLotNumber = 1;

            final ReviewId expectedReviewId = new ReviewId(1l);
            final ReviewSearchResults actualResult = sut.searchByReviewId(expectedReviewId);

            final ReviewSearchCondition actualCondition = actualResult.getCondition();
            final List<ReviewSearchResult> actualList = actualResult.getResults();

            assertThat(actualCondition, is(not(nullValue())));
            assertThat(actualCondition.getTargetCount(), is(0));
            assertThat(actualCondition.getLotPerCount(), is(expectedPagePerCount));
            assertThat(actualCondition.getLotNumber(), is(expectedLotNumber));
            assertThat(actualCondition.getOrderType(), is(ReviewSearchOrderType.PostTimeDesc));

            assertThat(actualList.size(), is(expectedPagePerCount));

            assertThat(actualResult.getAllCount(), is(expectedAllCount));
            assertThat(actualResult.hasNextLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(false));
        }
    }
}
