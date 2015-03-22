package jp.ne.glory.application.user;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepository;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.search.UserSearchCondition;
import jp.ne.glory.domain.user.value.search.UserSearchResults;

/**
 * ユーザの検索を行う.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class UserSearch {

    /**
     * ユーザリポジトリ.
     */
    private final UserRepository repository;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    protected UserSearch() {

        this.repository = null;
    }

    /**
     * コンストラクタ.
     *
     * @param repository リポジトリ
     */
    @Inject
    public UserSearch(final UserRepository repository) {

        this.repository = repository;
    }

    /**
     * 全てのユーザを取得する.
     *
     * @return ユーザリスト
     */
    public List<User> getAll() {

        return repository.findAll();
    }

    /**
     * ログインIDでユーザを検索する.
     *
     * @param loginId ログインID
     * @return ユーザ
     */
    public Optional<User> searchBy(final LoginId loginId) {

        return repository.findBy(loginId);
    }

    /**
     * ユーザの検索を行う.
     *
     * @param condition 検索条件
     * @return 検索結果
     */
    public UserSearchResults search(final UserSearchCondition condition) {

        final List<User> userResulst = repository.search(condition);
        final int resultCount = repository.getSearchCount(condition);

        final UserSearchResults results = new UserSearchResults(condition, userResulst, resultCount);

        return results;
    }
}
