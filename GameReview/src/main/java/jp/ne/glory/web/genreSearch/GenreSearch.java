package jp.ne.glory.web.genreSearch;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jp.ne.glory.application.genre.GenreList;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.ui.genre.GenreSearchResultView;
import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewListView;
import jp.ne.glory.web.common.WebPageConst;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ジャンル検索リソース.
 *
 * @author Junki Yamada
 */
@RequestScoped
@Path("genreSearch")
public class GenreSearch {

    /**
     * レビュー検索.
     */
    private final ReviewSearch search;

    /**
     * ジャンルリスト.
     */
    private final GenreList genreList;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    @Deprecated
    GenreSearch() {
        search = null;
        genreList = null;
    }

    /**
     * コンストラクタ.
     *
     * @param search レビュー検索
     * @param genreList ジャンルリスト
     */
    @Inject
    public GenreSearch(final ReviewSearch search, final GenreList genreList) {

        this.search = search;
        this.genreList = genreList;
    }

    /**
     * ジャンル検索を行う.
     *
     * @param paramGenreId 検索ジャンルID
     * @return Viewable ジャンル検索結果画面
     */
    @GET
    @Path("id/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable view(@PathParam("id") final long paramGenreId) {

        System.out.println("vie : " + paramGenreId);
        final GenreId genreId = new GenreId(paramGenreId);
        
        final ReviewListView reviewView
                = new ReviewListView(search.searchReviewByGenre(genreId, WebPageConst.PAGE_PER_SEARCH_REVIEWS, 1));
        final GenreSearchView genreSearchView = new GenreSearchView(genreList.getAllGenres());
        final GenreSearchResultView genreSearchResult = new GenreSearchResultView(genreSearchView, reviewView);

        return new Viewable("/genreSearch/genreSearchResult", genreSearchResult);
    }
}
