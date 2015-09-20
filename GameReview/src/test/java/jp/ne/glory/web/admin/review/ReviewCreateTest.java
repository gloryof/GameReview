package jp.ne.glory.web.admin.review;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.datetime.DateTimeCalculatorStub;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.application.genre.GenreSearchStub;
import jp.ne.glory.application.review.ReviewPost;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.repository.ReviewRepositoryStub;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import jp.ne.glory.test.game.search.GameSearchDataGenerator;
import jp.ne.glory.ui.admin.game.GameBean;
import jp.ne.glory.ui.admin.review.ReviewBean;
import jp.ne.glory.ui.admin.review.ReviewEditView;
import jp.ne.glory.ui.admin.review.ScoreBean;
import jp.ne.glory.web.admin.game.GameDetail;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ReviewCreateTest {

    public static class viewのテスト {

        private ReviewCreate sut = null;
        private ReviewRepositoryStub reviewRepoStub = null;
        private GameSearch gameSearchStub = null;
        private GameRepositoryStub gameRepoStub = null;
        private GenreSearchStub genreSearchStub = null;
        private DateTimeCalculatorStub dateTimeStub = null;

        @Before
        public void setUp() {

            final Set<Long> genreIdValues = new HashSet<>();
            gameRepoStub = new GameRepositoryStub();
            GameSearchDataGenerator.creaeteGames(100).stream()
                    .forEach(v -> {
                        genreIdValues.add(v.getGenreId().getValue());
                        gameRepoStub.save(v);
                    });

            final List<Genre> genreList = genreIdValues.stream()
                    .map(v -> {
                        final GenreId genreId = new GenreId(v);
                        final GenreName genreName = new GenreName("ジャンル" + v);

                        return new Genre(genreId, genreName);
                    })
                    .collect(Collectors.toList());
            genreSearchStub = new GenreSearchStub(genreList);

            gameSearchStub = new GameSearch(gameRepoStub);
            reviewRepoStub = new ReviewRepositoryStub();
            dateTimeStub = new DateTimeCalculatorStub();

            final ReviewPost post = new ReviewPost(reviewRepoStub, gameRepoStub, dateTimeStub);

            sut = new ReviewCreate(post, gameSearchStub, genreSearchStub);
        }
        
        @Test
        public void ゲームIDに紐づくゲームレビュー投稿画面が表示される() {

            final long expectedGameId = 5l;
            final Response actualResponse = sut.view(expectedGameId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable actualViewable = (Viewable) actualResponse.getEntity();

            assertThat(actualViewable.getTemplateName(), is(PagePaths.REVIEW_CREATE));
            assertThat(actualViewable.getModel(), is(instanceOf(ReviewEditView.class)));

            final ReviewEditView actualView = (ReviewEditView) actualViewable.getModel();

            final ReviewBean actualReview = actualView.getReview();

            assertThat(actualReview.getReviewId(), is(nullValue()));
            assertThat(actualReview.getGameId(), is(expectedGameId));
            assertThat(actualReview.getGoodPoint(), is(""));
            assertThat(actualReview.getBadPoint(), is(""));
            assertThat(actualReview.getComment(), is(""));

            final ScoreBean actuScore = actualReview.getScore();

            assertThat(actuScore.getAddiction(), is(ScorePoint.NotInput));
            assertThat(actuScore.getLoadTime(), is(ScorePoint.NotInput));
            assertThat(actuScore.getMusic(), is(ScorePoint.NotInput));
            assertThat(actuScore.getOperability(), is(ScorePoint.NotInput));
            assertThat(actuScore.getStory(), is(ScorePoint.NotInput));

            final DateTimeValue actualPostDatetime = actualReview.getPostDateTime();
            final DateTimeValue actualLastUpdateDateTime = actualReview.getLastUpdateDateTime();

            assertThat(actualPostDatetime.getValue(), is(nullValue()));
            assertThat(actualLastUpdateDateTime.getValue(), is(nullValue()));

            final GameBean actualGame = actualReview.getGame();
            final Game expectedGame = gameRepoStub.findBy(new GameId(expectedGameId)).get();
            final Genre expectedGenre = genreSearchStub.searchBy(expectedGame.getGenreId()).get();
            assertThat(actualGame.getGameId(), is(expectedGame.getId().getValue()));
            assertThat(actualGame.getTitle(), is(expectedGame.getTitle().getValue()));
            assertThat(actualGame.getSiteUrl(), is(expectedGame.getUrl().getValue()));
            assertThat(actualGame.getCeroRating(), is(expectedGame.getCeroRating()));
            assertThat(actualGame.getGenreName(), is(expectedGenre.getName().getValue()));
        }

        @Test
        public void 指定したゲームIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long reviewId = Long.MAX_VALUE;
            final Response actualResponse = sut.view(reviewId);

            final String base = UriBuilder.fromResource(GameDetail.class).toTemplate();
            final String append = UriBuilder.fromMethod(GameDetail.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(reviewId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }
    
    public static class createのテスト {

        private ReviewCreate sut = null;
        private ReviewRepositoryStub reviewRepoStub = null;
        private GameSearch gameSearchStub = null;
        private GameRepositoryStub gameRepoStub = null;
        private GenreSearchStub genreSearchStub = null;
        private DateTimeCalculatorStub dateTimeStub = null;

        @Before
        public void setUp() {

            final Set<Long> genreIdValues = new HashSet<>();
            gameRepoStub = new GameRepositoryStub();
            GameSearchDataGenerator.creaeteGames(100).stream()
                    .forEach(v -> {
                        genreIdValues.add(v.getGenreId().getValue());
                        gameRepoStub.save(v);
                    });

            final List<Genre> genreList = genreIdValues.stream()
                    .map(v -> {
                        final GenreId genreId = new GenreId(v);
                        final GenreName genreName = new GenreName("ジャンル" + v);

                        return new Genre(genreId, genreName);
                    })
                    .collect(Collectors.toList());
            genreSearchStub = new GenreSearchStub(genreList);

            gameSearchStub = new GameSearch(gameRepoStub);
            reviewRepoStub = new ReviewRepositoryStub();
            dateTimeStub = new DateTimeCalculatorStub();

            final ReviewPost post = new ReviewPost(reviewRepoStub, gameRepoStub, dateTimeStub);

            sut = new ReviewCreate(post, gameSearchStub, genreSearchStub);
        }

        @Test
        public void 正常な値が入力されている場合_保存されて_詳細画面を表示する() {

            final long paramGameId = 5L;

            final ReviewBean postedReview = new ReviewBean();
            postedReview.setGameId(paramGameId);

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

            final Game expectedGame = gameSearchStub.searchBy(new GameId(paramGameId)).get();
            final Genre expectedGenre = genreSearchStub.searchBy(expectedGame.getGenreId()).get();
            final GameBean game = new GameBean(expectedGame, expectedGenre);
            postedReview.setGame(game);

            final long expectedReviewSequence = reviewRepoStub.getCurrentSequence();

            final Response actualResponse = sut.create(paramGameId, postedReview);
            final String templatePath = UriBuilder.fromResource(ReviewDetail.class).toTemplate();
            final URI uri = UriBuilder.fromUri(templatePath).resolveTemplate("reviewId", expectedReviewSequence).build();

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(uri));

            final ReviewId expectedReviewId = new ReviewId(expectedReviewSequence);
            final Review actualReview = reviewRepoStub.findBy(expectedReviewId).get();

            assertThat(actualReview.getId().getValue(), is(expectedReviewId.getValue()));

            final Score actualScore = actualReview.getScore();
            assertThat(actualScore.getAddiction(), is(inputScore.getAddiction()));
            assertThat(actualScore.getLoadTime(), is(inputScore.getLoadTime()));
            assertThat(actualScore.getMusic(), is(inputScore.getMusic()));
            assertThat(actualScore.getOperability(), is(inputScore.getOperability()));
            assertThat(actualScore.getStory(), is(inputScore.getStory()));

            assertThat(actualReview.getGoodPoint().getValue(), is(postedReview.getGoodPoint()));
            assertThat(actualReview.getBadPoint().getValue(), is(postedReview.getBadPoint()));
            assertThat(actualReview.getComment().getValue(), is(postedReview.getComment()));

            final DateTimeValue expectedTime = dateTimeStub.getCurrentDateTime();
            final LocalDateTime actualPostedDateTime = actualReview.getPostTime().getValue().getValue();
            assertThat(actualPostedDateTime, is(expectedTime.getValue()));

            final LocalDateTime actualUpdateTime = actualReview.getLastUpdate().getValue().getValue();
            assertThat(actualUpdateTime, is(expectedTime.getValue()));
        }

        @Test
        public void ドメイン入力チェックエラーの場合_作成画面を表示する() {

            final long paramGameId = 5L;
            final ReviewBean postedReview = new ReviewBean();
            postedReview.setGameId(paramGameId);

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

            final Response actualResponse = sut.create(paramGameId, postedReview);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.OK));
            assertThat(actualResponse.getEntity(), is(instanceOf(Viewable.class)));

            final Viewable viewable = (Viewable) actualResponse.getEntity();

            assertThat(viewable.getTemplateName(), is(PagePaths.REVIEW_CREATE));
            assertThat(viewable.getModel(), is(instanceOf(ReviewEditView.class)));

            final ReviewEditView actualView = (ReviewEditView) viewable.getModel();
            final ReviewBean actualReview = actualView.getReview();

            final Game expectedGame = gameSearchStub.searchBy(new GameId(paramGameId)).get();
            final Genre expectedGenre = genreSearchStub.searchBy(expectedGame.getGenreId()).get();

            assertThat(actualReview.getReviewId(), is(postedReview.getReviewId()));
            assertThat(actualReview.getGameId(), is(paramGameId));

            final ScoreBean actualScore = actualReview.getScore();
            assertThat(actualScore.getAddiction(), is(inputScore.getAddiction()));
            assertThat(actualScore.getLoadTime(), is(inputScore.getLoadTime()));
            assertThat(actualScore.getMusic(), is(inputScore.getMusic()));
            assertThat(actualScore.getOperability(), is(inputScore.getOperability()));
            assertThat(actualScore.getStory(), is(inputScore.getStory()));

            assertThat(actualReview.getGoodPoint(), is(postedReview.getGoodPoint()));
            assertThat(actualReview.getBadPoint(), is(postedReview.getBadPoint()));
            assertThat(actualReview.getComment(), is(postedReview.getComment()));

            final GameBean actualGame = actualReview.getGame();
            assertThat(actualGame.getGameId(), is(expectedGame.getId().getValue()));
            assertThat(actualGame.getTitle(), is(expectedGame.getTitle().getValue()));
            assertThat(actualGame.getSiteUrl(), is(expectedGame.getUrl().getValue()));
            assertThat(actualGame.getCeroRating(), is(expectedGame.getCeroRating()));
            assertThat(actualGame.getGenreName(), is(expectedGenre.getName().getValue()));
        }

        @Test
        public void URLのゲームIDと入力値のゲームIDがとなる場合_Badリクエストエラーになる() {

            final long paramGameId = 5L;
            final ReviewBean inputData = new ReviewBean();
            inputData.setGameId(paramGameId);

            final Response actualResponse = sut.create(10l, inputData);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void URLのゲームIDが未設定の場合_Badリクエストエラーになる() {

            final long paramGameId = 5L;
            final ReviewBean inputData = new ReviewBean();
            inputData.setGameId(paramGameId);

            final Response actualResponse = sut.create(null, inputData);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 入力値のゲームIDが未設定の場合_Badリクエストエラーになる() {

            final long paramGameId = 5L;
            final ReviewBean inputData = new ReviewBean();

            final Response actualResponse = sut.create(paramGameId, inputData);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        }

        @Test
        public void 指定したゲームIDが存在しない場合_エラー画面にリダイレクトされる() {

            final long paramGameId = Long.MAX_VALUE;
            final ReviewBean inputData = new ReviewBean();
            inputData.setGameId(paramGameId);

            final Response actualResponse = sut.create(paramGameId, inputData);

            final String base = UriBuilder.fromResource(GameDetail.class).toTemplate();
            final String append = UriBuilder.fromMethod(GameDetail.class, "notFound").toTemplate();
            final URI uri = UriBuilder.fromUri(base + append).build(paramGameId);

            assertThat(actualResponse.getStatusInfo(), is(Response.Status.SEE_OTHER));
            assertThat(actualResponse.getLocation(), is(UriBuilder.fromUri(uri).build()));
        }
    }
}
