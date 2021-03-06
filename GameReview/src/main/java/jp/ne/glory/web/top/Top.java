package jp.ne.glory.web.top;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewView;
import jp.ne.glory.ui.top.TopView;
import jp.ne.glory.web.common.PagePaths;
import jp.ne.glory.web.common.WebPageConst;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * Topリソース.
 *
 * @author Junki Yamada
 */
@Path("top")
@RequestScoped
public class Top {

    /**
     * レビュー検索.
     */
    private final ReviewSearch search;

    /**
     * ジャンルリスト.
     */
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    @Deprecated
    Top() {
        this.search = null;
        this.genreSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param reviewSearch レビュー検索
     * @param genreSearch ジャンル検索
     */
    @Inject
    public Top(final ReviewSearch reviewSearch, final GenreSearch genreSearch) {

        search = reviewSearch;
        this.genreSearch = genreSearch;
    }

    /**
     * 画面を表示する.
     *
     * @return ビューオブジェクト
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable view() {

        final ReviewView reviewView = new ReviewView(search.searchNewReviews(WebPageConst.PAGE_PER_REVIEWS, 1));
        final GenreSearchView genreSearchView = new GenreSearchView(genreSearch.getAllGenres());
        final TopView topView = new TopView(genreSearchView, reviewView);

        return new Viewable(PagePaths.TOP, topView);
    }
}
