package jp.ne.glory.web.login;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import jp.ne.glory.ui.login.LoginView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ログインリソース.
 *
 * @author Junki Yamada
 */
@RequestScoped
@Path("/login")
public class Login {

    /**
     * ログイン画面表示.
     *
     * @return ログイン画面
     */
    @GET
    public Viewable view() {

        return new Viewable(PagePaths.LOGIN, new LoginView());
    }

    @POST
    public Viewable login() {

        return null;
    }
}
