package jp.ne.glory.domain.user.value.search;

import java.util.List;
import jp.ne.glory.domain.common.value.SearchResults;
import jp.ne.glory.domain.user.entity.User;

/**
 * ユーザ検索結果.
 *
 * @author Junki Yamada
 */
public class UserSearchResults extends SearchResults<UserSearchCondition, User> {

    /**
     * コンストラクタ.
     *
     * @param condition 検索条件
     * @param userList ユーザリスト
     * @param allCount 全件件数
     */
    public UserSearchResults(final UserSearchCondition condition, final List<User> userList, final int allCount) {

        super(condition, userList, allCount);
    }
}
