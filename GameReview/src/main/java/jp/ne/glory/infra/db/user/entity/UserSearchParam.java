package jp.ne.glory.infra.db.user.entity;

import jp.ne.glory.domain.user.value.search.UserSearchCondition;
import lombok.Getter;

/**
 * ユーザ検索条件.
 *
 * @author Junki Yamada
 */
public class UserSearchParam {

    /**
     * ログインID.
     */
    @Getter
    private final String loginId;

    @Getter
    private final String userName;

    /**
     * コンストラクタ.
     *
     * @param condition 検索条件
     */
    public UserSearchParam(final UserSearchCondition condition) {

        this.loginId = condition.getLoginId().getValue();
        this.userName = condition.getUserName().getValue();
    }
}
