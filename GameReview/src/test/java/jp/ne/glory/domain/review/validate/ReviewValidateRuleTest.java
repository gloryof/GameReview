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

public class ReviewValidateRuleTest {

    private static Review createBaseReveiw(final Long reviewIdValue) {

        return createBaseReveiw(reviewIdValue, null);
    }

    private static Review createBaseReveiw(final Long reviewIdValue, final Long gameIdValue) {

        final Review review = new Review(new ReviewId(reviewIdValue));

        if (gameIdValue != null) {

            review.gameId = new GameId(gameIdValue);
        }
        review.gooodPoint = new GoodPoint("良い点：テスト");
        review.badPoint = new BadPoint("悪い点：テスト");
        review.comment = new Comment("コメント：テスト");
        review.score = new Score();
        review.score.story = ScorePoint.Point5;
        review.score.operability = ScorePoint.Point4;
        review.score.addiction = ScorePoint.Point3;
        review.score.music = ScorePoint.Point2;
        review.score.loadTime = ScorePoint.Point1;

        return review;
    }

    public static class 全ての値が正常に設定されている場合 {

        private ReviewValidateRule sut = null;

        @Before
        public void setUp() {

            final Game game = new Game(new GameId(100L), new Title("タイトル"));
            final Review review = createBaseReveiw(123L, game.id.value);

            game.genreId = new GenreId(200L);
            game.ceroRating = CeroRating.A;
            game.url = new SiteUrl("http://test.co.jp");

            sut = new ReviewValidateRule(review, game);
        }

        @Test
        public void validateForRegisterを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validateForRegister();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void validateForEditを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validateForEdit();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class ゲームが未登録でゲーム情報が未設定の場合 {

        private ReviewValidateRule sut = null;

        @Before
        public void setUp() {

            final Review review = createBaseReveiw(123L);

            sut = new ReviewValidateRule(review, null);
        }

        @Test
        public void validateForRegisterでゲーム情報が未登録のエラーになる() {

            final ValidateErrors actual = sut.validateForRegister();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotInputInfo, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

        @Test
        public void validateForEditでゲーム情報が未登録のエラーになる() {

            final ValidateErrors actual = sut.validateForEdit();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotInputInfo, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }

    public static class ゲームIDと異なるゲームが紐付いている場合 {

        private ReviewValidateRule sut = null;

        @Before
        public void setUp() {

            final Game game = new Game(new GameId(100L), new Title("タイトル"));
            final Review review = createBaseReveiw(123L, (game.id.value + 1));

            game.genreId = new GenreId(200L);
            game.ceroRating = CeroRating.A;
            game.url = new SiteUrl("http://test.co.jp");

            sut = new ReviewValidateRule(review, game);
        }

        @Test
        public void validateForRegisterでゲーム情報の紐付けミスマッチエラーになる() {

            final ValidateErrors actual = sut.validateForRegister();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.MismatchRelation, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

        @Test
        public void validateForEditでゲーム情報が紐付けミスマッチエラーになる() {

            final ValidateErrors actual = sut.validateForEdit();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.MismatchRelation, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }

    public static class ゲームIDが未設定の場合 {

        private ReviewValidateRule sut = null;

        @Before
        public void setUp() {

            final Game game = new Game(new GameId(100L), new Title("タイトル"));
            final Review review = createBaseReveiw(123L);


            game.genreId = new GenreId(200L);
            game.ceroRating = CeroRating.A;
            game.url = new SiteUrl("http://test.co.jp");

            sut = new ReviewValidateRule(review, game);
        }

        @Test
        public void validateForRegisterでゲーム情報の紐付け未設定エラーになる() {

            final ValidateErrors actual = sut.validateForRegister();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotSettingRelation, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

        @Test
        public void validateForEditでゲーム情報が紐付け未設定エラーになる() {

            final ValidateErrors actual = sut.validateForEdit();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotSettingRelation, Game.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }

    public static class 全ての項目が未設定の場合 {

        private ReviewValidateRule sut = null;

        @Before
        public void setUp() {

            final Review review = new Review(ReviewId.notNumberingValue());
            final Game game = new Game(GameId.notNumberingValue(), Title.empty());

            sut = new ReviewValidateRule(review, game);
        }

        @Test
        public void validateForRegisterで必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validateForRegister();

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

        @Test
        public void validateForEditで全ての必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validateForEdit();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotRegister, Review.LABEL));
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
