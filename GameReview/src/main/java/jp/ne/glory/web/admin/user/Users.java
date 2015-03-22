package jp.ne.glory.web.admin.user;

import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import jp.ne.glory.application.user.UserSearch;
import jp.ne.glory.domain.user.value.search.UserSearchResults;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.user.UserBean;
import jp.ne.glory.ui.admin.user.UserList;
import jp.ne.glory.ui.admin.user.UserSearchConditionBean;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ユーザ一覧の表示処理.
 *
 * @author Junki Yamada
 */
@Path("/admin/users")
@RequestScoped
@CertifyTarget
public class Users {

    /**
     * ユーザ検索.
     */
    private final UserSearch userSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    Users() {
        this.userSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param userSearch ユーザ検索
     */
    @Inject
    public Users(final UserSearch userSearch) {

        this.userSearch = userSearch;
    }

    /**
     * ユーザ一覧の初期表示.
     *
     * @return　ユーザ一覧
     */
    @GET
    public Viewable view() {

        final UserList userList = new UserList();
        final List<UserBean> users = userSearch.getAll().stream().map(UserBean::new).collect(Collectors.toList());

        userList.setUsers(users);
        userList.setCondition(new UserSearchConditionBean());

        return new Viewable(PagePaths.USER_LIST, userList);
    }

    /**
     * ユーザの検索を行う.
     *
     * @param searchCondition 検索条件
     * @return ユーザ一覧
     */
    @POST
    @Path("search")
    public Viewable search(@BeanParam final UserSearchConditionBean searchCondition) {

        final UserList userList = new UserList();

        final UserSearchResults results = userSearch.search(searchCondition.createEntity());
        final List<UserBean> users = results.getResults().stream().map(UserBean::new).collect(Collectors.toList());

        userList.setUsers(users);
        userList.setCondition(searchCondition);

        return new Viewable(PagePaths.USER_LIST, userList);
    }
}
