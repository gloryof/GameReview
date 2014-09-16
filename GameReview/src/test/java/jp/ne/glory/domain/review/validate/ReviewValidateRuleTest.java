/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ne.glory.domain.review.validate;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.test.validate.ValidateErrorsHelper;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ReviewValidateRuleTest {

    public static class 全ての値が正常に設定されている場合 {

        private ReviewValidateRule sut = null;

        @Before
        public void setUp() {

            final Review review = new Review(new ReviewId(123L));

            review.gooodPoint = new GoodPoint("良い点：テスト");
            review.badPoint = new BadPoint("悪い点：テスト");
            review.comment = new Comment("コメント：テスト");
            review.score = new Score();
            review.score.story = ScorePoint.Point5;
            review.score.operability = ScorePoint.Point4;
            review.score.addiction = ScorePoint.Point3;
            review.score.music = ScorePoint.Point2;
            review.score.loadTime = ScorePoint.Point1;

            sut = new ReviewValidateRule(review);
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
    
    public static class 全ての項目が未設定の場合 {

        private ReviewValidateRule sut = null;

        @Before
        public void setUp() {

            final Review review = new Review(ReviewId.notNumberingValue());

            sut = new ReviewValidateRule(review);
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

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }
}
