package jp.ne.glory.infra.db.user.entity;

import jp.ne.glory.domain.user.value.search.UserSearchCondition;
import jp.ne.glory.infra.db.common.entity.RecordLimits;
import lombok.Getter;

/**
 * ユーザ検索条件.
 *
 * @author Junki Yamada
 */
public class UserSearchParam {

    /**
     * レコード制限.
     */
    @Getter
    private final RecordLimits limits;

    /**
     * ログインID.
     */
    @Getter
    private final String loginId;

    /**
     * ユーザ名.s
     */
    @Getter
    private final String userName;

    /**
     * コンストラクタ.
     *
     * @param condition 検索条件
     */
    public UserSearchParam(final UserSearchCondition condition) {

        this.limits = new RecordLimits(condition);
        this.loginId = condition.getLoginId().getValue();
        this.userName = condition.getUserName().getValue();
    }
}
