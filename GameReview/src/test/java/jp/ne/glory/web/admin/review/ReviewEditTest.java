package jp.ne.glory.web.admin.review;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.datetime.DateTimeCalculator;
import jp.ne.glory.application.datetime.DateTimeCalculatorStub;
import jp.ne.glory.application.review.ReviewPost;
import jp.ne.glory.application.review.ReviewSearchStub;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.repository.ReviewRepositoryStub;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.test.review.search.ReviewSearchDataGenerator;
import jp.ne.glory.ui.admin.game.GameBean;
import jp.ne.glory.ui.admin.review.ReviewBean;
import jp.ne.glory.ui.admin.review.ReviewEditView;
import jp.ne.glory.ui.admin.review.ScoreBean;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReviewEditTest {

    public static class viewのテスト {

        private ReviewEdit sut = null;
        private ReviewSearchStub stub = null;

        @Before
        public void setUp() {

            List<ReviewSearchResult> reviewList = ReviewSearchDataGenerator.createBaseSearchResults(200);

            stub = new ReviewSearchStub(reviewList);
            final ReviewPost post = new ReviewPost(new ReviewRepositoryStub(), new GameRepositoryStub(),
                    new DateTimeCalculator());
            sut = new ReviewEdit(stub, post);
        }

        @Test
        public void レビューIDに紐づく編集画面が表示される() {

            final long expectedReviewId = 5l;
            final Response actualResponse = sut.view(expectedReviewId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable actualViewable = (Viewable) actualResponse.getEntity();

            assertThat(actualViewable.getTemplateName(), is(PagePaths.REVIEW_EDIT));
            assertThat(actualViewable.getModel(), is(instanceOf(ReviewEditView.class)));

            final ReviewEditView actualView = (ReviewEditView) actualViewable.getModel();
            final ReviewSearchResult result = stub.searchByReviewId(new ReviewId(expectedReviewId)).get();

            final Review expectedReview = result.getReview();
            final ReviewBean actualReview = actualView.getReview();

            assertThat(actualReview.getReviewId(), is(expectedReview.getId().getValue()));
            assertThat(actualReview.getGoodPoint(), is(expectedReview.getGoodPoint().getValue()));
            assertThat(actualReview.getBadPoint(), is(expectedReview.getBadPoint().getValue()));
            assertThat(actualReview.getComment(), is(expectedReview.getComment().getValue()));

            final Score expectedScore = expectedReview.getScore();
            final ScoreBean actuScore = actualReview.getScore();
            assertThat(actuScore.getAddiction(), is(expectedScore.getAddiction()));
            assertThat(actuScore.getLoadTime(), is(expectedScore.getLoadTime()));
            assertThat(actuScore.getMusic(), is(expectedScore.getMusic()));
            assertThat(actuScore.getOperability(), is(expectedScore.getOperability()));
            assertThat(actuScore.getStory(), is(expectedScore.getStory()));

            final DateTimeValue actualPostDatetime = actualReview.getPostDateTime();
            final DateTimeValue expcetedPostDatetime = expectedReview.getPostTime().getValue();

            assertThat(actualPostDatetime.getValue(), is(expcetedPostDatetime.getValue()));

            final DateTimeValue actualLastUpdateDateTime = actualReview.getLastUpdateDateTime();
            final DateTimeValue expctedLastUpdateDateTime = expectedReview.getLastUpdate().getValue();

            assertThat(actualLastUpdateDateTime.getValue(), is(expctedLastUpdateDateTime.getValue()));

            final GameBean actualGame = actualReview.getGame();
            final Game expectedGame = result.getGame();
            final Genre expectedGenre = result.getGenre();
            assertThat(actualGame.getGameId(), is(expectedGame.getId().getValue()));
            assertThat(actualGame.getTitle(), is(expectedGame.getTitle().getValue()));
            assertThat(actualGame.getSiteUrl(), is(expectedGame.getUrl().getValue()));
            assertThat(actualGame.getCeroRating(), is(expectedGame.getCeroRating()));
            assertThat(actualGame.getGenreName(), is(expectedGenre.getName().getValue()));
        }

        @Test
        public void 指定したレビューIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long reviewId = Long.MAX_VALUE;
            final Response actualResponse = sut.view(reviewId);

            final String base = UriBuilder.fromResource(ReviewDetail.class).toTemplate();
            final String append = UriBuilder.fromMethod(ReviewDetail.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(reviewId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }

    public static class completeEditのテスト {

        private ReviewEdit sut = null;
        private ReviewSearchStub reviewSearchStub = null;
        private ReviewRepositoryStub reviewRepStub = null;
        private GameRepositoryStub gameRepStub = null;
        private DateTimeCalculatorStub calculatorStub = null;

        @Before
        public void setUp() {

            List<ReviewSearchResult> reviewList = ReviewSearchDataGenerator.createBaseSearchResults(200);

            reviewRepStub = new ReviewRepositoryStub();
            gameRepStub = new GameRepositoryStub();
            calculatorStub = new DateTimeCalculatorStub();

            reviewList.forEach(v -> {
                reviewRepStub.save(v.getReview());
                gameRepStub.save(v.getGame());
            });

            reviewSearchStub = new ReviewSearchStub(reviewList);
            final ReviewPost post = new ReviewPost(reviewRepStub, gameRepStub, calculatorStub);
            sut = new ReviewEdit(reviewSearchStub, post);
        }

        @Test
        public void 正常な値が入力されている場合_保存されて_詳細画面を表示する() {

            final long paramReviewId = 5L;
            final ReviewBean postedReview = new ReviewBean();

            final ReviewSearchResult beforeUpdate = reviewSearchStub.searchByReviewId(new ReviewId(paramReviewId)).get();
            final PostDateTime beforeUpdatePostDateTime = beforeUpdate.getReview().getPostTime();

            postedReview.setReviewId(paramReviewId);

            final ScoreBean inputScore = new ScoreBean();

            inputScore.setAddiction(ScorePoint.Point2);
            inputScore.setLoadTime(ScorePoint.Point3);
            inputScore.setMusic(ScorePoint.Point4);
            inputScore.setOperability(ScorePoint.Point5);
            inputScore.setStory(ScorePoint.Point1);
            postedReview.setScore(inputScore);

            postedReview.setGoodPoint("良い点");
            postedReview.setBadPoint("悪い点");
            postedReview.setComment("コメント");

            final ReviewEditView inputData = new ReviewEditView();
            inputData.setReview(postedReview);

            final Response actualResponse = sut.completeEdit(paramReviewId, postedReview);
            final String templatePath = UriBuilder.fromResource(ReviewDetail.class).toTemplate();
            final URI uri = UriBuilder.fromUri(templatePath).resolveTemplate("reviewId", paramReviewId).build();

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(uri));

            final Review actualReview = reviewRepStub.findBy(new ReviewId(paramReviewId)).get();

            assertThat(actualReview.getId().getValue(), is(postedReview.getReviewId()));

            final Score actualScore = actualReview.getScore();
            assertThat(actualScore.getAddiction(), is(inputScore.getAddiction()));
            assertThat(actualScore.getLoadTime(), is(inputScore.getLoadTime()));
            assertThat(actualScore.getMusic(), is(inputScore.getMusic()));
            assertThat(actualScore.getOperability(), is(inputScore.getOperability()));
            assertThat(actualScore.getStory(), is(inputScore.getStory()));

            assertThat(actualReview.getGoodPoint().getValue(), is(postedReview.getGoodPoint()));
            assertThat(actualReview.getBadPoint().getValue(), is(postedReview.getBadPoint()));
            assertThat(actualReview.getComment().getValue(), is(postedReview.getComment()));

            final LocalDateTime actualPostedDateTime = actualReview.getPostTime().getValue().getValue();
            assertThat(actualPostedDateTime, is(beforeUpdatePostDateTime.getValue().getValue()));

            final LocalDateTime actualUpdateTime = actualReview.getLastUpdate().getValue().getValue();
            assertThat(actualUpdateTime, is(calculatorStub.getCurrentDateTime().getValue()));
        }

        @Test
        public void ドメイン入力チェックエラーの場合_編集画面を表示する() {

            final long paramReviewId = 5L;
            final ReviewBean postedReview = new ReviewBean();

            postedReview.setReviewId(paramReviewId);

            final ScoreBean inputScore = new ScoreBean();

            inputScore.setAddiction(ScorePoint.Exclued);
            inputScore.setLoadTime(ScorePoint.Exclued);
            inputScore.setMusic(ScorePoint.Exclued);
            inputScore.setOperability(ScorePoint.Exclued);
            inputScore.setStory(ScorePoint.Exclued);
            postedReview.setScore(inputScore);

            postedReview.setGoodPoint("良い点");
            postedReview.setBadPoint("悪い点");
            postedReview.setComment("コメント");

            final Response actualResponse = sut.completeEdit(paramReviewId, postedReview);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.REVIEW_EDIT));
            assertThat(viewable.getModel(), is(instanceOf(ReviewEditView.class)));

            final ReviewEditView actualView = (ReviewEditView) viewable.getModel();
            final ReviewBean actualReview = actualView.getReview();

            final ReviewSearchResult searchResult
                    = reviewSearchStub.searchByReviewId(new ReviewId(paramReviewId)).get();
            final Game expectedGame = searchResult.getGame();

            assertThat(actualReview.getReviewId(), is(postedReview.getReviewId()));

            final ScoreBean actualScore = actualReview.getScore();
            assertThat(actualScore.getAddiction(), is(inputScore.getAddiction()));
            assertThat(actualScore.getLoadTime(), is(inputScore.getLoadTime()));
            assertThat(actualScore.getMusic(), is(inputScore.getMusic()));
            assertThat(actualScore.getOperability(), is(inputScore.getOperability()));
            assertThat(actualScore.getStory(), is(inputScore.getStory()));

            assertThat(actualReview.getGoodPoint(), is(postedReview.getGoodPoint()));
            assertThat(actualReview.getBadPoint(), is(postedReview.getBadPoint()));
            assertThat(actualReview.getComment(), is(postedReview.getComment()));

            final Review beforeData = searchResult.getReview();
            final LocalDateTime actualPostedDateTime = actualReview.getPostDateTime().getValue();
            assertThat(actualPostedDateTime, is(beforeData.getPostTime().getValue().getValue()));

            final LocalDateTime actualUpdateTime = actualReview.getLastUpdateDateTime().getValue();
            assertThat(actualUpdateTime, is(beforeData.getLastUpdate().getValue().getValue()));
        }

        @Test
        public void URLのレビューIDと入力値のレビューIDがとなる場合_Badリクエストエラーになる() {

            final long paramReviewId = 5L;
            final ReviewBean inputData = new ReviewBean();
            inputData.setReviewId(paramReviewId);

            final Response actualResponse = sut.completeEdit(10l, inputData);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 入力値のレビューIDが未設定の場合_Badリクエストエラーになる() {

            final long paramReviewId = 5L;
            final ReviewBean inputData = new ReviewBean();

            final Response actualResponse = sut.completeEdit(paramReviewId, inputData);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 指定したレビューIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long paramReviewId = Long.MAX_VALUE;
            final ReviewBean inputData = new ReviewBean();
            inputData.setReviewId(paramReviewId);

            final Response actualResponse = sut.completeEdit(paramReviewId, inputData);

            final String base = UriBuilder.fromResource(ReviewDetail.class).toTemplate();
            final String append = UriBuilder.fromMethod(ReviewDetail.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(paramReviewId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }
}
