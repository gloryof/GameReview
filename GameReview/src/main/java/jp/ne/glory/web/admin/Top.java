package jp.ne.glory.web.admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * 管理画面TOP
 *
 * @author JunkiYamada
 */
@Path("/admin")
@CertifyTarget
public class Top {

    /**
     * 管理画面TOPを表示する.
     *
     * @return 管理画面TOP
     */
    @GET
    public Viewable view() {

        return new Viewable(PagePaths.ADMIN_TOP);
    }
}
