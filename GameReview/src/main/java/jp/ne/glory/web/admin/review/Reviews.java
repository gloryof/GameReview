
package jp.ne.glory.web.admin.review;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.review.ReviewListView;
import jp.ne.glory.ui.admin.review.ReviewSearchConditionBean;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * レビュー一覧.
 *
 * @author Junki Yamada
 */
@CertifyTarget
@Path("/admin/reviews")
@RequestScoped
public class Reviews {

    /**
     * レビュー検索.
     */
    private final ReviewSearch search;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    public Reviews() {

        this.search = null;
    }

    /**
     * コンストラクタ
     *
     * @param search レビュー検索
     */
    @Inject
    public Reviews(final ReviewSearch search) {

        this.search = search;
    }

    /**
     * 一覧表示.
     *
     * @return レビュー一覧
     */
    @GET
    @Transactional(Transactional.TxType.REQUIRED)
    public Viewable view() {

        final ReviewSearchResults result = search.searchNewReviews(20, 1);
        final ReviewListView view = new ReviewListView(result, new ReviewSearchConditionBean());

        final Viewable viewable = new Viewable(PagePaths.REVIEW_LIST, view);

        return viewable;
    }
}
