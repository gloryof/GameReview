/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.glory.domain.game.validate;

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
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.test.validate.ValidateErrorsHelper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class GameValidateRuleTest {

    public static class 全ての値が正常に設定されている場合 {
        
        private GameValidateRule sut = null;

        @Before
        public void setUp() {

            final GameId gameId = new GameId(12L);
            final Title title = new Title("テストタイトル");

            final Game game = new Game(gameId, title);
            game.setUrl(new SiteUrl("http://test.co.jp/index"));
            game.setGenreId(new GenreId(10L));
            game.setCeroRating(CeroRating.A);

            sut = new GameValidateRule(game);
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
        
        private GameValidateRule sut = null;

        @Before
        public void setUp() {

            final GameId gameId = GameId.notNumberingValue();
            final Title title = Title.empty();

            final Game game = new Game(gameId, title);

            sut = new GameValidateRule(game);
        }

        @Test
        public void validateForRegisterで必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validateForRegister();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.Required, Title.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, CeroRating.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Genre.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

        @Test
        public void validateForEditで全ての必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validateForEdit();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.NotRegister, Game.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Title.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, CeroRating.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Genre.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }
}
