package jp.ne.glory.web.top;

import java.time.LocalDateTime;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.web.top.bean.TopInfo;
import org.glassfish.jersey.server.mvc.Viewable;

@Path("top")
public class Top {

    @GET
    public Viewable test() {

        final TopInfo topInfo = new TopInfo(LocalDateTime.now(), "Taro", "Yamada");

        return new Viewable("/top/top", topInfo);
    }
}
