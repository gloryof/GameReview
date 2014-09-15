/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.glory.domain.review.value;

import static org.junit.Assert.*;
import static jp.ne.glory.test.validate.ValidateMatcher.validatedBy;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.review.value.Score;
import jp.ne.groly.domain.review.value.ScorePoint;

import org.junit.Before;
import org.junit.Test;

public class ScoreTest {

    public static class 全てが入力されている場合 {

        private Score sut = null;

        @Before
        public void setUp() {

            sut = new Score();

            sut.addiction = ScorePoint.Point5;
            sut.loadTime = ScorePoint.Point4;
            sut.music = ScorePoint.Point3;
            sut.operability = ScorePoint.Point2;
            sut.story = ScorePoint.Point1;
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 全てが未入力の場合 {

        private Score sut = null;

        @Before
        public void setUp() {

            sut = new Score();

            sut.addiction = ScorePoint.NotInput;
            sut.loadTime = ScorePoint.NotInput;
            sut.music = ScorePoint.NotInput;
            sut.operability = ScorePoint.NotInput;
            sut.story = ScorePoint.NotInput;
        }

        @Test
        public void validateで必須入力エラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.RequiredSelectOne, Score.LABEL);
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }

        @Test
        public void 熱中度に点数を設定すると必須入力エラーにならない() {

            sut.addiction = ScorePoint.Point4;

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void ロード時間に点数を設定すると必須入力エラーにならない() {

            sut.loadTime = ScorePoint.Point3;

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void 音楽に点数を設定すると必須入力エラーにならない() {

            sut.music = ScorePoint.Point2;

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void 操作性に点数を設定すると必須入力エラーにならない() {

            sut.operability = ScorePoint.Point1;

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void ストーリーに点数を設定すると必須入力エラーにならない() {

            sut.story = ScorePoint.Point1;

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 未入力が1つでもある場合 {

        private Score sut = null;

        @Before
        public void setUp() {

            sut = new Score();

            sut.addiction = ScorePoint.Point1;
            sut.loadTime = ScorePoint.Point0;
            sut.music = ScorePoint.Point5;
            sut.operability = ScorePoint.Point4;
            sut.story = ScorePoint.NotInput;
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 入力1つ以上_評価対象外が1つ以上ある場合 {

        private Score sut = null;

        @Before
        public void setUp() {

            sut = new Score();

            sut.addiction = ScorePoint.Exclued;
            sut.loadTime = ScorePoint.Point0;
            sut.music = ScorePoint.Exclued;
            sut.operability = ScorePoint.Exclued;
            sut.story = ScorePoint.Exclued;
        }

        @Test
        public void validateを行っても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 全てが評価対象外の場合 {

        private Score sut = null;

        @Before
        public void setUp() {

            sut = new Score();

            sut.addiction = ScorePoint.Exclued;
            sut.loadTime = ScorePoint.Exclued;
            sut.music = ScorePoint.Exclued;
            sut.operability = ScorePoint.Exclued;
            sut.story = ScorePoint.Exclued;
        }

        @Test
        public void validateで選択エラーになる() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(true));

            final List<ValidateError> actualList = actualErrors.toList();

            final ValidateError expectedError = new ValidateError(ErrorInfo.SelectedPattern, Score.LABEL, "点数");
            assertThat(actualList.size(), is(1));
            assertThat(actualList.get(0), is(validatedBy(expectedError)));
        }
    }
}
