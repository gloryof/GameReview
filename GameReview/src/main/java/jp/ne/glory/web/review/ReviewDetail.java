package jp.ne.glory.web.review;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewView;
import jp.ne.glory.ui.top.TopView;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * レビュー詳細.
 *
 * @author Junki Yamada
 */
@Path("review")
public class ReviewDetail {

    /**
     * レビュー検索.
     */
    private final ReviewSearch search;

    /**
     * ジャンル検索.
     */
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    @Deprecated
    ReviewDetail() {
        search = null;
        genreSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param search レビュー検索
     * @param genreSearch ジャンル検索
     */
    @Inject
    public ReviewDetail(final ReviewSearch search, final GenreSearch genreSearch) {

        this.search = search;
        this.genreSearch = genreSearch;
    }

    /**
     * 詳細画面表示.
     *
     * @param paramReviewId レビューID
     * @return TOP画面
     */
    @GET
    @Path("id/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable view(@PathParam("id") final long paramReviewId) {

        final ReviewId reviewId = new ReviewId(paramReviewId);
        final ReviewView reviewView = new ReviewView(search.searchByReviewId(reviewId).get());
        final GenreSearchView genreSearchView = new GenreSearchView(genreSearch.getAllGenres());
        final TopView topView = new TopView(genreSearchView, reviewView);

        return new Viewable(PagePaths.TOP, topView);
    }
}
