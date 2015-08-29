package jp.ne.glory.web.admin.review;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * レビュー詳細.
 *
 * @author Junki Yamada.
 */
@RequestScoped
@Path("/admin/review/{reviewId}/")
@CertifyTarget
public class ReviewDetail {

    /**
     * レビューが見つからなかった場合の画面を表示する.
     *
     * @return エラー画面
     */
    @Path("notFound")
    @GET
    public Viewable notFound() {

        return new Viewable(PagePaths.REVIEW_NOT_FOUND);
    }
}
