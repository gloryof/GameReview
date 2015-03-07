package jp.ne.glory.ui.admin.user;

import lombok.Getter;
import lombok.Setter;

/**
 * ユーザ検索Bean.
 *
 * @author Junki Yamada
 */
public class UserSearchConditionBean {

    /**
     * ユーザID.
     */
    @Getter
    @Setter
    private Long userId;

    /**
     * ユーザ名.
     */
    @Getter
    @Setter
    private String userName;

    /**
     * ログインID
     */
    @Getter
    @Setter
    private String loginId;
}
