package jp.ne.glory.domain.user.flow;

import java.util.function.Function;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepository;
import jp.ne.glory.domain.user.validate.UserValidateRule;
import jp.ne.glory.domain.user.value.UserId;

/**
 * ユーザ登録処理.
 *
 * @author Junki Yamada
 */
public class UserRegister {

    /**
     * ユーザリポジトリ.
     */
    private final UserRepository repository;

    /**
     * コンストラクタ.
     *
     * @param paramRepository リポジトリ
     */
    public UserRegister(final UserRepository paramRepository) {

        repository = paramRepository;
    }

    /**
     * ユーザを登録する.
     *
     * @param user ユーザ
     * @return 登録結果
     */
    public UserRegisterResult register(final User user) {

        final Function<User, ValidateErrors> checkFunc = (v -> {

            final UserValidateRule rule = new UserValidateRule(v);
            return rule.validateForRegister();
        });

        return save(user, checkFunc);
    }

    /**
     * ユーザの編集を終了する.
     *
     * @param user ユーザ
     * @return 登録結果
     */
    public UserRegisterResult finishEdit(final User user) {

        final Function<User, ValidateErrors> checkFunc = (v -> {

            final UserValidateRule rule = new UserValidateRule(v);
            return rule.validateForEdit();
        });

        return save(user, checkFunc);
    }

    /**
     * 保存処理.<br>
     * パラメータで受け渡したcheckFuncの結果、エラーがなければ登録を行う。
     *
     * @param user ユーザ
     * @param checkFunc チェック関数
     * @return 登録結果
     */
    private UserRegisterResult save(final User user, final Function<User, ValidateErrors> checkFunc) {

        final ValidateErrors errors = checkFunc.apply(user);

        if (errors.hasError()) {

            return new UserRegisterResult(errors, UserId.notNumberingValue());
        }

        final UserId userId = repository.save(user);

        return new UserRegisterResult(errors, userId);
    }
}
