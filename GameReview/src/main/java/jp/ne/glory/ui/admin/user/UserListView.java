package jp.ne.glory.ui.admin.user;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザ一覧.
 *
 * @author Junki Yamada
 */
public class UserListView {

    /**
     * ユーザ検索条件.
     */
    @Getter
    @Setter
    private UserSearchConditionBean condition;

    /**
     * ユーザ情報一覧.
     */
    @Getter
    @Setter
    private List<UserBean> users;
}
