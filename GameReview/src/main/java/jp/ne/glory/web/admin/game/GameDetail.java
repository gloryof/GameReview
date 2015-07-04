package jp.ne.glory.web.admin.game;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.infra.certify.CertifyTarget;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ゲーム詳細.
 *
 * @author Junki Yamada.
 */
@RequestScoped
@Path("/admin/game/{gameId}")
@CertifyTarget
public class GameDetail {

    @GET
    public Viewable view() {

        return null;
    }
}
