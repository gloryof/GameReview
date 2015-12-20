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
     * LIMIT件数.
     */
    @Getter
    private Integer limit = null;

    /**
     * OFFSET件数.
     */
    @Getter
    private Integer offset = null;

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

        if (0 < condition.getTargetCount()) {

            this.limit = condition.getTargetCount();

            final int lpc = condition.getLotPerCount();
            final int ln = condition.getLotNumber();
            this.offset = lpc * (ln - 1);
        }

        this.loginId = condition.getLoginId().getValue();
        this.userName = condition.getUserName().getValue();
    }
}
