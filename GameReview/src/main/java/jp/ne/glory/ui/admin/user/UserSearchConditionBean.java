package jp.ne.glory.ui.admin.user;

import java.util.Optional;
import javax.ws.rs.FormParam;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.domain.user.value.search.UserSearchCondition;
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

    /**
     * ユーザ検索条件エンティティを作成する.
     *
     * @return ユーザ検索条件エンティティ
     */
    public UserSearchCondition createEntity() {

        final UserSearchCondition entity = new UserSearchCondition();

        final UserName name = new UserName(Optional.ofNullable(userName).orElse(""));
        final LoginId id = new LoginId(Optional.ofNullable(loginId).orElse(""));

        entity.setUserName(name);
        entity.setLoginId(id);

        return entity;
    }
}
