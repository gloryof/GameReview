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


public class GameModifyCommonValidateRuleTest {

    public static class 全ての値が正常に設定されている場合 {
        
        private GameModifyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final GameId gameId = new GameId(12L);
            final Title title = new Title("テストタイトル");

            final Game game = new Game(gameId);
            game.setTitle(title);
            game.setUrl(new SiteUrl("http://test.co.jp/index"));
            game.setGenreId(new GenreId(10L));
            game.setCeroRating(CeroRating.A);

            sut = new GameModifyCommonValidateRule(game);
        }

        @Test
        public void validateを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 全ての項目が未設定の場合 {
        
        private GameModifyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final GameId gameId = GameId.notNumberingValue();

            final Game game = new Game(gameId);

            sut = new GameModifyCommonValidateRule(game);
        }

        @Test
        public void validateで必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.Required, Title.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, CeroRating.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Genre.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }
}
