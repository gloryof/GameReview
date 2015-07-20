/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ne.glory.domain.review.validate;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import jp.ne.glory.test.validate.ValidateErrorsHelper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReviewModifyCommonValidateRuleTest {

    private static Review createBaseReveiw(final Long reviewIdValue) {

        return createBaseReveiw(reviewIdValue, null);
    }

    private static Review createBaseReveiw(final Long reviewIdValue, final Long gameIdValue) {

        final Review review = new Review(new ReviewId(reviewIdValue));

        if (gameIdValue != null) {

            review.setGameId(new GameId(gameIdValue));
        }
        review.setGoodPoint(new GoodPoint("良い点：テスト"));
        review.setBadPoint(new BadPoint("悪い点：テスト"));
        review.setComment(new Comment("コメント：テスト"));
        review.setScore(new Score());
        review.getScore().setStory(ScorePoint.Point5);
        review.getScore().setOperability(ScorePoint.Point4);
        review.getScore().setAddiction(ScorePoint.Point3);
        review.getScore().setMusic(ScorePoint.Point2);
        review.getScore().setLoadTime(ScorePoint.Point1);

        return review;
    }

    public static class 全ての値が正常に設定されている場合 {

        private ReviewModifyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final Game game = new Game(new GameId(100L));
            game.setTitle(new Title("タイトル"));
            final Review review = createBaseReveiw(123L, game.getId().getValue());

            game.setGenreId(new GenreId(200L));
            game.setCeroRating(CeroRating.A);
            game.setUrl(new SiteUrl("http://test.co.jp"));

            sut = new ReviewModifyCommonValidateRule(review, game);
        }

        @Test
        public void validateを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }

    }

    public static class ゲームが未登録でゲーム情報が未設定の場合 {

        private ReviewModifyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final Review review = createBaseReveiw(123L);

            sut = new ReviewModifyCommonValidateRule(review, null);
        }

        @Test
        public void validateでゲーム情報が未登録のエラーになる() {

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotInputInfo, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }

    public static class ゲームIDと異なるゲームが紐付いている場合 {

        private ReviewModifyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final Game game = new Game(new GameId(100L));
            game.setTitle(new Title("タイトル"));
            final Review review = createBaseReveiw(123L, (game.getId().getValue() + 1));

            game.setGenreId(new GenreId(200L));
            game.setCeroRating(CeroRating.A);
            game.setUrl(new SiteUrl("http://test.co.jp"));

            sut = new ReviewModifyCommonValidateRule(review, game);
        }

        @Test
        public void validateでゲーム情報の紐付けミスマッチエラーになる() {

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.MismatchRelation, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

    }

    public static class ゲームIDが未設定の場合 {

        private ReviewModifyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final Game game = new Game(new GameId(100L));
            game.setTitle(new Title("タイトル"));
            final Review review = createBaseReveiw(123L);

            game.setGenreId(new GenreId(200L));
            game.setCeroRating(CeroRating.A);
            game.setUrl(new SiteUrl("http://test.co.jp"));

            sut = new ReviewModifyCommonValidateRule(review, game);
        }

        @Test
        public void validateでゲーム情報の紐付け未設定エラーになる() {

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotSettingRelation, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

    }

    public static class 全ての項目が未設定の場合 {

        private ReviewModifyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final Review review = new Review(ReviewId.notNumberingValue());
            final Game game = new Game(GameId.notNumberingValue());

            sut = new ReviewModifyCommonValidateRule(review, game);
        }

        @Test
        public void validateで必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.Required, GoodPoint.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, BadPoint.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Comment.LABEL));
            errorList.add(new ValidateError(ErrorInfo.RequiredSelectOne, Score.LABEL));
            errorList.add(new ValidateError(ErrorInfo.NotSettingRelation, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }
}
