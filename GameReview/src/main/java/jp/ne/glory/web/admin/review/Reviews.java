
package jp.ne.glory.web.admin.review;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.common.type.DateValue;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
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
    private final ReviewSearch reviewSearch;

    /**
     * ジャンル検索.
     */
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    public Reviews() {

        this.reviewSearch = null;
        this.genreSearch = null;
    }

    /**
     * コンストラクタ
     *
     * @param reviewSearch レビュー検索
     * @param genreSearch ジャンル検索
     */
    @Inject
    public Reviews(final ReviewSearch reviewSearch, final GenreSearch genreSearch) {

        this.reviewSearch = reviewSearch;
        this.genreSearch = genreSearch;
    }

    /**
     * 一覧表示.
     *
     * @return レビュー一覧
     */
    @GET
    @Transactional(Transactional.TxType.REQUIRED)
    public Viewable view() {

        final ReviewSearchResults result = reviewSearch.searchNewReviews(20, 1);
        final ReviewSearchConditionBean condition = new ReviewSearchConditionBean(genreSearch.getAllGenres());

        final ReviewListView view = new ReviewListView(result, condition);
        final Viewable viewable = new Viewable(PagePaths.REVIEW_LIST, view);

        return viewable;
    }

    /**
     * 検索を行う.
     *
     * @param condition 検索条件
     * @return 一覧画面
     */
    @GET
    @Path("search")
    @Transactional(Transactional.TxType.REQUIRED)
    public Viewable search(@BeanParam final ReviewSearchConditionBean condition) {

        final ReviewSearchResults result = reviewSearch.search(createCondition(condition));

        final ReviewListView view = new ReviewListView(result, condition);
        final Viewable viewable = new Viewable(PagePaths.REVIEW_LIST, view);

        return viewable;
    }

    /**
     * 検索条件を作成する.
     *
     * @param input 入力した検索条件
     * @return 検索条件
     */
    private ReviewSearchCondition createCondition(final ReviewSearchConditionBean input) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();

        if (input.getGenreId() != null) {

            condition.addGenreId(new GenreId(input.getGenreId()));
        }

        if (input.getCeroRating() != null) {

            condition.addCeroRating(input.getCeroRating());
        }

        if (input.getTitle() != null && !input.getTitle().isEmpty()) {

            condition.setTitle(input.getTitle());
        }

        if (input.getFrom() != null) {

            condition.setPostedFrom(new DateValue(input.getFrom()));
        }

        if (input.getTo() != null) {

            condition.setPostedTo(new DateValue(input.getTo()));
        }

        condition.setLotNumber(input.getPageNumber());
        return condition;
    }
}
