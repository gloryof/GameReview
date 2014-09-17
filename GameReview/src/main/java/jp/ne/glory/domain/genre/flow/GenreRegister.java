package jp.ne.glory.domain.genre.flow;

import java.util.function.Function;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepository;
import jp.ne.glory.domain.genre.validate.GenreValidateRule;
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
            final GenreValidateRule rule = new GenreValidateRule(v);
            return rule.validateForRegister();
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
            final GenreValidateRule rule = new GenreValidateRule(v);
            return rule.validateForEdit();
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