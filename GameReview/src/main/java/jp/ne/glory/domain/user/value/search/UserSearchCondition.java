package jp.ne.glory.domain.user.value.search;

import jp.ne.glory.domain.common.value.SearchCondition;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.UserName;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザ検索条件.
 *
 * @author Junki Yamada
 */
public class UserSearchCondition extends SearchCondition {

    /**
     * ログインID.
     */
    @Getter
    @Setter
    private LoginId loginId = null;

    /**
     * ユーザ名.
     */
    @Getter
    @Setter
    private UserName userName = null;
}
