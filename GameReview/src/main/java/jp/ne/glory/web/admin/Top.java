package jp.ne.glory.web.admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * 管理画面TOP
 *
 * @author JunkiYamada
 */
@Path("/admin")
public class Top {

    @GET
    public Viewable view() {

        return new Viewable(PagePaths.ADMIN_TOP);
    }
}
