package jp.ne.glory.web.top;

import java.time.LocalDateTime;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jp.ne.glory.web.top.bean.TopInfo;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * Top画面.
 *
 * @author Admin
 */
@Path("top")
public class Top {

    /**
     * 画面を表示する.
     *
     * @return ビューオブジェクト
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable view() {

        final TopInfo topInfo = new TopInfo(LocalDateTime.now(), "Taro", "Yamada");

        return new Viewable("/top/top", topInfo);
    }
}
