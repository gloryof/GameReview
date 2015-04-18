package jp.ne.glory.ui.admin.user;

import java.util.List;
import jp.ne.glory.domain.user.value.Authority;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザ一覧.
 *
 * @author Junki Yamada
 */
public class UserListView {

    /**
     * 権限ラベル（設定変更）
     */
    @Getter
    private final String authorityConfigChangeLabel = Authority.ConfigChange.name;

    /**
     * 権限ラベル（レビュー投稿）
     */
    @Getter
    private final String authorityReviewPostLabel = Authority.ReviewPost.name;

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
