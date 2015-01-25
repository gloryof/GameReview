package jp.ne.glory.application.review;

import java.util.Optional;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepositoryStub;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.repository.ReviewRepositoryStub;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.ScorePoint;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReviewPostTest {

    private static class TestTool {

        private static Review copy(final Review review) {

            final Review copiedReview = new Review(new ReviewId(review.getId().getValue()));

            copiedReview.setBadPoint(new BadPoint(review.getBadPoint().getValue()));
            copiedReview.setComment(new Comment(review.getComment().getValue()));
            copiedReview.setGameId(new GameId(review.getGameId().getValue()));
            copiedReview.setGoodPoint(new GoodPoint(review.getGoodPoint().getValue()));
            copiedReview.getScore().setAddiction(review.getScore().getAddiction());
            copiedReview.getScore().setLoadTime(review.getScore().getLoadTime());
            copiedReview.getScore().setMusic(review.getScore().getMusic());
            copiedReview.getScore().setOperability(review.getScore().getOperability());
            copiedReview.getScore().setStory(review.getScore().getStory());

            return copiedReview;
        }

        private static Game copy(final Game game) {

            final Game copiedGame = new Game(game.getId(), game.getTitle());

            copiedGame.setCeroRating(game.getCeroRating());
            copiedGame.setGenreId(game.getGenreId());
            copiedGame.setUrl(game.getUrl());

            return copiedGame;
        }

        private static Review createBaseReview() {

            return createBaseReview(ReviewId.notNumberingValue(), GameId.notNumberingValue());
        }

        private static Review createBaseReview(final GameId gameId) {

            return createBaseReview(ReviewId.notNumberingValue(), gameId);
        }

        private static Review createBaseReview(final ReviewId reviewId, final GameId gameId) {

            final Review review = new Review(new ReviewId(12L));

            review.setBadPoint(new BadPoint("悪い点テスト"));
            review.setComment(new Comment("コメントテスト"));
            review.setGoodPoint(new GoodPoint("良い点テスト"));
            review.getScore().setAddiction(ScorePoint.Point5);
            review.setGameId(gameId);

            return review;
        }

        private static Review createInvalidReview() {

            return createInvalidReview(GameId.notNumberingValue());
        }

        private static Review createInvalidReview(final GameId gameId) {

            final Review review = createBaseReview(ReviewId.notNumberingValue(), gameId);

            review.setComment(Comment.empty());

            return review;
        }

        private static Game createBaseGame() {

            return createBaseGame(GameId.notNumberingValue());
        }

        private static Game createBaseGame(final GameId gameId) {

            final Game game = new Game(gameId, new Title("タイトル"));

            game.setCeroRating(CeroRating.A);
            game.setGenreId(new GenreId(12L));

            return game;
        }

        private static Game createInvalidGame() {

            return new Game(GameId.notNumberingValue(), Title.empty());
        }

        private static Game createInvalidGame(final GameId gameId) {

            return new Game(gameId, Title.empty());
        }
    }

    public static class postのテスト {

        private ReviewPost sut = null;
        private ReviewRepositoryStub reviewRepStub = null;
        private GameRepositoryStub gameRepStub = null;
        private Game game = null;

        @Before
        public void setUp() {

            reviewRepStub = new ReviewRepositoryStub();
            gameRepStub = new GameRepositoryStub();
            sut = new ReviewPost(reviewRepStub, gameRepStub);

            game = TestTool.createBaseGame(new GameId(100L));
            gameRepStub.save(game);
        }

        @Test
        public void 正常に値が設定されていれば投稿される() {

            final Review review = TestTool.createBaseReview(game.getId());

            final ReviewPostResult actualResult = sut.post(review);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(false));

            final Optional<Review> postedReview = reviewRepStub.findBy(actualResult.getPostedReviewId());
            assertThat(postedReview.isPresent(), is(true));
        }

        @Test
        public void 入力値に不正がある場合はエラーになる() {

            final Review review = TestTool.createInvalidReview(game.getId());

            final ReviewPostResult actualResult = sut.post(review);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));
        }

        @Test
        public void ゲームとの紐付きが不正な場合はエラーになる() {

            final GameId invalidGameId = new GameId(game.getId().getValue() + 1);
            final Review review = TestTool.createBaseReview(invalidGameId);

            final ReviewPostResult actualResult = sut.post(review);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));
        }

        @Test
        public void ゲームとの紐付きが設定されていない場合はエラーになる() {

            final Review review = TestTool.createBaseReview(GameId.notNumberingValue());

            final ReviewPostResult actualResult = sut.post(review);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));
        }
    }

    public static class postWithGameのテスト {

        private ReviewPost sut = null;
        private ReviewRepositoryStub reviewRepStub = null;
        private GameRepositoryStub gameRepStub = null;

        @Before
        public void setUp() {

            reviewRepStub = new ReviewRepositoryStub();
            gameRepStub = new GameRepositoryStub();
            sut = new ReviewPost(reviewRepStub, gameRepStub);
        }

        @Test
        public void 正常に値が設定されていれば投稿される() {

            final Review review = TestTool.createBaseReview();
            final Game game = TestTool.createBaseGame();

            final ReviewPostResult actualResult = sut.postWithGame(review, game);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(false));

            final Optional<Review> postedReviewOption = reviewRepStub.findBy(actualResult.getPostedReviewId());
            assertThat(postedReviewOption.isPresent(), is(true));

            final Review postedReview = postedReviewOption.get();

            final Optional<Game> savedGameOption = gameRepStub.findBy(postedReview.getGameId());
            assertThat(savedGameOption.isPresent(), is(true));
        }

        @Test
        public void レビューの入力値に不正がある場合はエラーになる() {

            final Review review = TestTool.createInvalidReview();
            final Game game = TestTool.createBaseGame();

            final ReviewPostResult actualResult = sut.postWithGame(review, game);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));
        }

        @Test
        public void ゲームの入力値に不正がある場合はエラーになる() {

            final Review review = TestTool.createBaseReview();
            final Game game = TestTool.createInvalidGame();

            final ReviewPostResult actualResult = sut.postWithGame(review, game);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));
        }

        @Test
        public void ゲームがNullの場合はエラーになる() {

            final Review review = TestTool.createBaseReview();

            final ReviewPostResult actualResult = sut.postWithGame(review, null);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));
        }
    }

    public static class repostのテスト {

        private ReviewPost sut = null;
        private ReviewRepositoryStub reviewRepStub = null;
        private GameRepositoryStub gameRepStub = null;
        private ReviewId reviewId = null;

        @Before
        public void setUp() {

            reviewRepStub = new ReviewRepositoryStub();
            gameRepStub = new GameRepositoryStub();
            sut = new ReviewPost(reviewRepStub, gameRepStub);

            final Game game = TestTool.createBaseGame(new GameId(100L));
            gameRepStub.save(game);

            reviewId = new ReviewId(12L);
            final Review review = TestTool.createBaseReview(reviewId, game.getId());
            reviewRepStub.save(review);
        }

        @Test
        public void 正常に値が設定されていれば再投稿される() {

            final Review review = TestTool.copy(reviewRepStub.findBy(reviewId).get());

            final Comment editedComment = new Comment("編集後コメント");
            review.setComment(editedComment);

            final ReviewPostResult actualResult = sut.repost(review);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(false));

            final Optional<Review> repostedReviewOption = reviewRepStub.findBy(actualResult.getPostedReviewId());
            assertThat(repostedReviewOption.isPresent(), is(true));

            final Review repostedReview = repostedReviewOption.get();
            assertThat(reviewId.isSame(repostedReview.getId()), is(true));
            assertThat(repostedReview.getComment().getValue(), is(editedComment.getValue()));
        }

        @Test
        public void 入力値に不正がある場合はエラーになる() {

            final Review review = TestTool.copy(reviewRepStub.findBy(reviewId).get());
            final Comment beforeComment = review.getComment();
            review.setComment(Comment.empty());

            final ReviewPostResult actualResult = sut.repost(review);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));

            final Review afterErrorReview = reviewRepStub.findBy(reviewId).get();
            assertThat(beforeComment.getValue(), is(afterErrorReview.getComment().getValue()));
        }

        @Test
        public void ゲームとの紐付きが不正な場合はエラーになる() {

            final Review review = TestTool.copy(reviewRepStub.findBy(reviewId).get());
            final GameId beforeGameId = review.getGameId();
            review.setGameId(new GameId(review.getId().getValue() + 1));

            final ReviewPostResult actualResult = sut.repost(review);

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));

            final Review afterErrorReview = reviewRepStub.findBy(reviewId).get();
            assertThat(beforeGameId.isSame(afterErrorReview.getGameId()), is(true));
        }
    }

    public static class repostWithGameのテスト {

        private ReviewPost sut = null;
        private ReviewRepositoryStub reviewRepStub = null;
        private GameRepositoryStub gameRepStub = null;
        private ReviewId reviewId = null;
        private GameId gameId = null;

        @Before
        public void setUp() {

            reviewRepStub = new ReviewRepositoryStub();
            gameRepStub = new GameRepositoryStub();
            sut = new ReviewPost(reviewRepStub, gameRepStub);

            gameId = new GameId(100L);
            final Game game = TestTool.createBaseGame(gameId);
            gameRepStub.save(game);

            reviewId = new ReviewId(12L);
            final Review review = TestTool.createBaseReview(reviewId, game.getId());
            reviewRepStub.save(review);
        }

        @Test
        public void 正常に値が設定されていれば投稿される() {

            final Review review = TestTool.copy(reviewRepStub.findBy(reviewId).get());
            final Game game = TestTool.copy(gameRepStub.findBy(gameId).get());

            final Comment changedComment = new Comment("変更後コメント");
            review.setComment(changedComment);

            final SiteUrl changedUrl = new SiteUrl("http://changed.test.co.jp");
            game.setUrl(changedUrl);

            final ReviewPostResult actualResult = sut.postWithGame(review, game);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(false));

            final Optional<Review> postedReviewOption = reviewRepStub.findBy(actualResult.getPostedReviewId());
            assertThat(postedReviewOption.isPresent(), is(true));

            final Review postedReview = postedReviewOption.get();
            assertThat(reviewId.isSame(postedReview.getId()), is(true));
            assertThat(postedReview.getComment().getValue(), is(changedComment.getValue()));

            final Optional<Game> savedGameOption = gameRepStub.findBy(postedReview.getGameId());
            assertThat(savedGameOption.isPresent(), is(true));

            final Game savedGame = savedGameOption.get();
            assertThat(savedGame.getUrl().getValue(), is(changedUrl.getValue()));
        }

        @Test
        public void レビューの入力値に不正がある場合はエラーになる() {

            final Review review = TestTool.copy(reviewRepStub.findBy(reviewId).get());
            final Game game = TestTool.copy(gameRepStub.findBy(gameId).get());

            final Comment beforeComment = review.getComment();
            review.setComment(Comment.empty());

            final ReviewPostResult actualResult = sut.repostWithGame(review, game);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));

            final Optional<Review> afterErrorReviewOption = reviewRepStub.findBy(reviewId);
            assertThat(afterErrorReviewOption.isPresent(), is(true));

            final Review afterErrorReview = afterErrorReviewOption.get();
            assertThat(afterErrorReview.getComment().getValue(), is(beforeComment.getValue()));
        }

        @Test
        public void ゲームの入力値に不正がある場合はエラーになる() {

            final Review review = TestTool.copy(reviewRepStub.findBy(reviewId).get());
            final Game game = TestTool.copy(gameRepStub.findBy(gameId).get());

            final SiteUrl beforeSiteUrl = game.getUrl();
            game.setUrl(new SiteUrl("テスト"));

            final ReviewPostResult actualResult = sut.repostWithGame(review, game);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));

            final Optional<Game> afterErrorGameOption = gameRepStub.findBy(gameId);
            assertThat(afterErrorGameOption.isPresent(), is(true));

            final Game afterErrorGame = afterErrorGameOption.get();
            assertThat(afterErrorGame.getUrl().getValue(), is(beforeSiteUrl.getValue()));
        }

        @Test
        public void ゲームがNullの場合はエラーになる() {

            final Review review = TestTool.copy(reviewRepStub.findBy(reviewId).get());
            final Game game = TestTool.copy(gameRepStub.findBy(gameId).get());

            final ReviewPostResult actualResult = sut.repostWithGame(review, null);
            final ValidateErrors actualErrors = actualResult.getErrors();
            assertThat(actualErrors.hasError(), is(true));

            final ReviewId actualReviewId = actualResult.getPostedReviewId();
            assertThat(actualReviewId.isSetValue(), is(false));
        }
    }
}
