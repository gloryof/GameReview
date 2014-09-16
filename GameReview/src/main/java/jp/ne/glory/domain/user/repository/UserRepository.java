package jp.ne.glory.domain.user.repository;

import java.util.Optional;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.UserId;

/**
 * ユーザリポジトリ.
 *
 * @author Junki Yamada
 */
public interface UserRepository {

    /**
     * ユーザを保存する.
     *
     * @param user ユーザ
     * @return 保存したユーザID
     */
    UserId save(final User user);

    /**
     * ユーザIDでユーザを探す.
     *
     * @param userId ユーザID
     * @return ユーザ
     */
    Optional<User> findBy(final UserId userId);
}
