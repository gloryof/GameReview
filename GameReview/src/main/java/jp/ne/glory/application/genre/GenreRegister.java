package jp.ne.glory.application.genre;

import java.util.function.Function;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepository;
import jp.ne.glory.domain.genre.validate.GenreEditValidateRule;
import jp.ne.glory.domain.genre.validate.GenreModfyCommonValidateRule;
import jp.ne.glory.domain.genre.value.GenreId;

/**
 * ジャンル登録処理.
 *
 * @author Junki Yamada
 */
public class GenreRegister {

    /**
     * リポジトリ.
     */
    private final GenreRepository repository;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    GenreRegister() {
        this.repository = null;
    }

    /**
     * コンストラクタ.
     *
     * @param paramRepository リポジトリ
     */
    public GenreRegister(final GenreRepository paramRepository) {

        repository = paramRepository;
    }

    /**
     * ジャンルを登録する.
     *
     * @param genre ジャンル.
     * @return 登録結果
     */
    public GenreRegisterResult register(final Genre genre) {

        final Function<Genre, ValidateErrors> checkFunc = (v -> {
            final GenreModfyCommonValidateRule rule = new GenreModfyCommonValidateRule(v);
            return rule.validate();
        });

        return save(genre, checkFunc);
    }

    /**
     * ジャンルの編集を終了する.
     *
     * @param genre ジャンル.
     * @return 登録結果
     */
    public GenreRegisterResult finishEdit(final Genre genre) {

        final Function<Genre, ValidateErrors> checkFunc = (v -> {
            final GenreEditValidateRule rule = new GenreEditValidateRule(v);
            return rule.validate();
        });

        return save(genre, checkFunc);
    }

    /**
     * 保存処理.<br>
     * パラメータで受け渡したcheckFuncの結果、エラーがなければ登録を行う。
     *
     * @param genre ジャンル
     * @param checkFunc チェック関数
     * @return 登録結果
     */
    private GenreRegisterResult save(final Genre genre, final Function<Genre, ValidateErrors> checkFunc) {

        final ValidateErrors errors = checkFunc.apply(genre);

        if (errors.hasError()) {

            return new GenreRegisterResult(errors, GenreId.notNumberingValue());
        }

        final GenreId genreId = repository.save(genre);

        return new GenreRegisterResult(errors, genreId);
    }
}
