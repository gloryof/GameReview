package jp.ne.glory.web.admin.review;

import java.net.URI;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.review.ReviewDetailView;
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
     * レビュー検索.
     */
    private final ReviewSearch search;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    protected ReviewDetail() {

        this.search = null;
    }

    /**
     * コンストラクタ.
     *
     * @param search レビュー検索
     */
    @Inject
    public ReviewDetail(final ReviewSearch search) {

        this.search = search;
    }

    /**
     * 詳細画面を表示する.
     *
     * @param paramReviewId レビューID
     * @return 詳細画面
     */
    @GET
    public Response view(@PathParam("reviewId") final long paramReviewId) {

        final Optional<ReviewSearchResult> resultOpt = search.searchByReviewId(new ReviewId(paramReviewId));

        if (!resultOpt.isPresent()) {

            return redirectNotFound(paramReviewId);
        }

        final ReviewSearchResult result = resultOpt.get();
        final ReviewDetailView detail = new ReviewDetailView(result);
        final Viewable viewable = new Viewable(PagePaths.REVIEW_DETAIL, detail);

        return Response.ok(viewable).build();
    }

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

    /**
     * ジャンルが見つからない画面にリダイレクトする.
     *
     * @param reviewId レビューID
     * @return リダイレクトレスポンス
     */
    private Response redirectNotFound(final long reviewId) {

        final String base = UriBuilder.fromResource(ReviewDetail.class).toTemplate();
        final String append = UriBuilder.fromMethod(ReviewDetail.class, "notFound").toTemplate();

        final UriBuilder builder = UriBuilder.fromUri(base + append);
        builder.resolveTemplate("reviewId", reviewId);

        final URI uri = builder.build();

        return Response.seeOther(uri).build();
    }

}
