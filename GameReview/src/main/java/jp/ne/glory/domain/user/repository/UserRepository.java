package jp.ne.glory.domain.user.repository;

import java.util.List;
import java.util.Optional;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.search.UserSearchCondition;

/**
 * ユーザリポジトリ.
 *
 * @author Junki Yamada
 */
public interface UserRepository {

    /**
     * 全てのユーザを取得する.
     *
     * @return ユーザリスト
     */
    List<User> findAll();

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

    /**
     * ログインIDでユーザを探す.
     *
     * @param loginId ログインID
     * @return ユーザ
     */
    Optional<User> findBy(final LoginId loginId);

    /**
     * ユーザの検索を行う.
     *
     * @param condition 検索条件
     * @return ユーザリスト
     */
    List<User> search(final UserSearchCondition condition);

    /**
     * ユーザの検索結果件数を取得する.
     *
     * @param condition 検索条件
     * @return 検索結果件数
     */
    int getSearchCount(final UserSearchCondition condition);
}
