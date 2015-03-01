package jp.ne.glory.application.user;

import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepository;
import jp.ne.glory.domain.user.value.LoginId;

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
     * ログインIDでユーザを検索する.
     *
     * @param loginId ログインID
     * @return ユーザ
     */
    public Optional<User> searchBy(final LoginId loginId) {

        return repository.findBy(loginId);
    }
}
