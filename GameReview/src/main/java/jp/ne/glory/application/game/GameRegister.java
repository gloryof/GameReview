package jp.ne.glory.application.game;

import java.util.function.Function;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepository;
import jp.ne.glory.domain.game.validate.GameEditValidateRule;
import jp.ne.glory.domain.game.validate.GameModifyCommonValidateRule;
import jp.ne.glory.domain.game.value.GameId;

/**
 * ゲーム登録処理.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class GameRegister {

    /**
     * リポジトリ.
     */
    private final GameRepository repository;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    GameRegister() {
        this.repository = null;
    }

    /**
     * コンストラクタ.
     *
     * @param paramRepository リポジトリ
     */
    @Inject
    public GameRegister(final GameRepository paramRepository) {

        repository = paramRepository;
    }

    /**
     * ゲームを登録する.
     *
     * @param game ゲーム
     * @return 登録結果
     */
    public GameRegisterResult register(final Game game) {

        final Function<Game, ValidateErrors> checkFunc = (v -> {
            final GameModifyCommonValidateRule rule = new GameModifyCommonValidateRule(v);
            return rule.validate();
        });

        return save(game, checkFunc);
    }

    /**
     * ゲームの編集を終了する.
     *
     * @param game ゲーム
     * @return 登録結果
     */
    public GameRegisterResult finishEdit(final Game game) {

        final Function<Game, ValidateErrors> checkFunc = (v -> {
            final GameEditValidateRule rule = new GameEditValidateRule(v);
            return rule.validate();
        });

        return save(game, checkFunc);
    }

    /**
     * 保存処理.<br>
     * パラメータで受け渡したcheckFuncの結果、エラーがなければ登録を行う。
     *
     * @param game ゲーム
     * @param checkFunc チェック関数
     * @return 登録結果
     */
    private GameRegisterResult save(final Game game, final Function<Game, ValidateErrors> checkFunc) {

        final ValidateErrors errors = checkFunc.apply(game);

        if (errors.hasError()) {

            return new GameRegisterResult(errors, GameId.notNumberingValue());
        }

        final GameId gameId = repository.save(game);

        return new GameRegisterResult(errors, gameId);
    }
}
