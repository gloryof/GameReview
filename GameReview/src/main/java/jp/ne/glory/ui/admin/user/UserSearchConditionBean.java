package jp.ne.glory.ui.admin.user;

import javax.ws.rs.FormParam;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザ検索Bean.
 *
 * @author Junki Yamada
 */
public class UserSearchConditionBean {

    /**
     * ユーザ名.
     */
    @Getter
    @Setter
    @FormParam("userName")
    private String userName;

    /**
     * ログインID
     */
    @Getter
    @Setter
    @FormParam("loginId")
    private String loginId;
}
