/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.glory.domain.review.value;

import java.util.List;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import org.junit.Before;
import org.junit.Test;

import static jp.ne.glory.test.validate.ValidateMatcher.validatedBy;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScoreTest {

    public static class 全てが入力されている場合 {

        private Score sut = null;

        @Before
        public void setUp() {

            sut = new Score();

            sut.setAddiction(ScorePoint.Point5);
            sut.setLoadTime(ScorePoint.Point4);
            sut.setMusic(ScorePoint.Point3);
            sut.setOperability(ScorePoint.Point2);
            sut.setStory(ScorePoint.Point1);
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

            sut.setAddiction(ScorePoint.NotInput);
            sut.setLoadTime(ScorePoint.NotInput);
            sut.setMusic(ScorePoint.NotInput);
            sut.setOperability(ScorePoint.NotInput);
            sut.setStory(ScorePoint.NotInput);
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

            sut.setAddiction(ScorePoint.Point4);

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void ロード時間に点数を設定すると必須入力エラーにならない() {

            sut.setLoadTime(ScorePoint.Point3);

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void 音楽に点数を設定すると必須入力エラーにならない() {

            sut.setMusic(ScorePoint.Point2);

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void 操作性に点数を設定すると必須入力エラーにならない() {

            sut.setOperability(ScorePoint.Point1);

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }

        @Test
        public void ストーリーに点数を設定すると必須入力エラーにならない() {

            sut.setStory(ScorePoint.Point1);

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 未入力が1つでもある場合 {

        private Score sut = null;

        @Before
        public void setUp() {

            sut = new Score();

            sut.setAddiction(ScorePoint.Point1);
            sut.setLoadTime(ScorePoint.Point0);
            sut.setMusic(ScorePoint.Point5);
            sut.setOperability(ScorePoint.Point4);
            sut.setStory(ScorePoint.NotInput);
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

            sut.setAddiction(ScorePoint.Exclued);
            sut.setLoadTime(ScorePoint.Point0);
            sut.setMusic(ScorePoint.Exclued);
            sut.setOperability(ScorePoint.Exclued);
            sut.setStory(ScorePoint.Exclued);
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

            sut.setAddiction(ScorePoint.Exclued);
            sut.setLoadTime(ScorePoint.Exclued);
            sut.setMusic(ScorePoint.Exclued);
            sut.setOperability(ScorePoint.Exclued);
            sut.setStory(ScorePoint.Exclued);
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
