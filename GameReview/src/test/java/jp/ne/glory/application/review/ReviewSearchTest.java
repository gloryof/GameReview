package jp.ne.glory.application.review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.repository.ReviewRepositoryStub;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchOrderType;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class ReviewSearchTest {

    public static ReviewSearch sut;

    public static class searchNewRevewsのテスト {

        @Test
        public void 検索結果が返却される() {

            final ReviewRepositoryStub repoStub = new ReviewRepositoryStub();
            final int expectedAllCount = 10;
            LongStream.rangeClosed(1, expectedAllCount).mapToObj(v -> {
                final Review review = new Review(new ReviewId(v));
                review.postTime = new PostDateTime(new DateTimeValue(LocalDateTime.now()));

                return review;
            }).forEach(repoStub::save);

            sut = new ReviewSearch(repoStub);
            final int expectedPagePerCount = 5;

            final ReviewSearchResults actualResult = sut.searchNewReviews(expectedPagePerCount);

            final ReviewSearchCondition actualCondition = actualResult.condition;
            final List<ReviewSearchResult> actualList = actualResult.results;

            assertThat(actualCondition, is(not(nullValue())));
            assertThat(actualCondition.targetCount, is(expectedPagePerCount));
            assertThat(actualCondition.lotPerCount, is(expectedPagePerCount));
            assertThat(actualCondition.orderType, is(ReviewSearchOrderType.PostTimeDesc));

            assertThat(actualList.size(), is(expectedPagePerCount));

            assertThat(actualResult.allCount, is(expectedAllCount));
            assertThat(actualResult.hasNetLot(), is(false));
            assertThat(actualResult.hasPrevLot(), is(false));
        }
    }
}
