package jp.ne.glory.web.admin.review;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.application.review.ReviewSearchStub;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.test.review.search.ReviewSearchDataGenerator;
import jp.ne.glory.ui.admin.game.GameBean;
import jp.ne.glory.ui.admin.review.ReviewBean;
import jp.ne.glory.ui.admin.review.ReviewDetailView;
import jp.ne.glory.ui.admin.review.ScoreBean;
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
public class ReviewDetailTest {


    public static class viewのテスト {

        private ReviewDetail sut = null;
        private ReviewSearch searchStub = null;

        @Before
        public void setUp() {

            List<ReviewSearchResult> reviewList = ReviewSearchDataGenerator.createBaseSearchResults(200);

            searchStub = new ReviewSearchStub(reviewList);
            sut = new ReviewDetail(searchStub);
        }

        @Test
        public void 指定したレビューIDに紐付くレビューが取得できる() {

            final long expectedReviewId = 4;

            final Response actualResponse = sut.view(expectedReviewId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.REVIEW_DETAIL));
            assertThat(viewable.getModel(), is(instanceOf(ReviewDetailView.class)));

            final ReviewDetailView actualView = (ReviewDetailView) viewable.getModel();

            final ReviewBean actualReview = actualView.getReview();
            final ReviewSearchResult expectedReview = searchStub.searchByReviewId(new ReviewId(expectedReviewId)).get();

            assertThat(actualReview.getReviewId(), is(expectedReview.getReview().getId().getValue()));
            assertThat(actualReview.getGoodPoint(), is(expectedReview.getReview().getGoodPoint().getValue()));
            assertThat(actualReview.getBadPoint(), is(expectedReview.getReview().getBadPoint().getValue()));
            assertThat(actualReview.getComment(), is(expectedReview.getReview().getComment().getValue()));

            final Score expectedScore = expectedReview.getReview().getScore();
            final ScoreBean actuScore = actualReview.getScore();
            assertThat(actuScore.getAddiction(), is(expectedScore.getAddiction()));
            assertThat(actuScore.getLoadTime(), is(expectedScore.getLoadTime()));
            assertThat(actuScore.getMusic(), is(expectedScore.getMusic()));
            assertThat(actuScore.getOperability(), is(expectedScore.getOperability()));
            assertThat(actuScore.getStory(), is(expectedScore.getStory()));

            final DateTimeValue actualPostDatetime = actualReview.getPostDateTime();
            final DateTimeValue expcetedPostDatetime = expectedReview.getReview().getPostTime().getValue();
            assertThat(actualPostDatetime.getValue(), is(expcetedPostDatetime.getValue()));

            final GameBean actualGame = actualReview.getGame();
            final Game expectedGame = expectedReview.getGame();
            final Genre expectedGenre = expectedReview.getGenre();
            assertThat(actualGame.getGameId(), is(expectedGame.getId().getValue()));
            assertThat(actualGame.getTitle(), is(expectedGame.getTitle().getValue()));
            assertThat(actualGame.getSiteUrl(), is(expectedGame.getUrl().getValue()));
            assertThat(actualGame.getCeroRating(), is(expectedGame.getCeroRating()));
            assertThat(actualGame.getGenreId(), is(expectedGenre.getId().getValue()));
            assertThat(actualGame.getGenreName(), is(expectedGenre.getName().getValue()));
        }

        @Test
        public void 指定したレビューIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long genreId = Long.MAX_VALUE;
            final Response actualResponse = sut.view(genreId);

            final String base = UriBuilder.fromResource(ReviewDetail.class).toTemplate();
            final String append = UriBuilder.fromMethod(ReviewDetail.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(genreId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }

    public static class notFoundのテスト {

        private ReviewDetail sut = null;

        @Before
        public void setUp() {

            sut = new ReviewDetail(new ReviewSearchStub(new ArrayList<>()));
        }

        @Test
        public void エラー画面が表示される() {

            final Viewable viewable = sut.notFound();

            assertThat(viewable.getTemplateName(), is(PagePaths.REVIEW_NOT_FOUND));
            assertThat(viewable.getModel(), is(nullValue()));
        }
    }
}
