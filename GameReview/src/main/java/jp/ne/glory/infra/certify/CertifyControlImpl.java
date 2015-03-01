package jp.ne.glory.infra.certify;

import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.application.user.UserSearch;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.LoginId;

/**
 * 認証をコントロールする.<br>
 * 認証はHTTPセッションで行う.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class CertifyControlImpl implements CertifyControl {

    /**
     * セッション情報.
     */
    private final CertifySession certify;

    /**
     * ユーザ検索.
     */
    private final UserSearch userSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    protected CertifyControlImpl() {

        certify = null;
        userSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param certify 認証情報
     * @param userSearch ユーザ検索
     */
    @Inject
    public CertifyControlImpl(final CertifySession certify, final UserSearch userSearch) {

        this.certify = certify;
        this.userSearch = userSearch;
    }

    /**
     * 認証情報をセッションに設定する.
     *
     * @param loginId ログインID
     */
    @Override
    public CertifySession createAuthentication(final LoginId loginId) {

        final Optional<User> optinalUser = userSearch.searchBy(loginId);

        optinalUser.ifPresent(user -> {
            certify.setActive(true);
            certify.setUserId(user.getUserId());
            certify.setUserName(user.getUserName());
        });

        return certify;
    }

}
