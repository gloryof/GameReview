package jp.ne.glory.domain.user.flow;

import java.util.Optional;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepository;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;

/**
 * ユーザ認証.
 *
 * @author Junki Yamada
 */
public class UserAuthentication {

    /**
     * ユーザリポジトリ.
     */
    private final UserRepository repository;

    /**
     * コンストラクタ.
     *
     * @param repository リポジトリ
     */
    public UserAuthentication(final UserRepository repository) {

        this.repository = repository;
    }

    /**
     * ユーザの認証を行う.
     *
     * @param loginId ログインID
     * @param password パスワード
     * @return 認証に成功した場合：true、認証に失敗した場合：false
     */
    public boolean isCertify(final LoginId loginId, final Password password) {

        final Optional<User> optional = repository.findBy(loginId);

        if (!optional.isPresent()) {

            return false;
        }

        final User user = optional.get();
        return user.password.isMatch(password);
    }
}
