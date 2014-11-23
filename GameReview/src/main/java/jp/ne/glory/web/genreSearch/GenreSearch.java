package jp.ne.glory.web.genreSearch;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.ui.genre.GenreSearchResultView;
import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewListView;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ジャンル検索リソース.
 *
 * @author Junki Yamada
 */
@Path("genreSearch")
@RequestScoped
public class GenreSearch {

    /**
     * レビュー検索.
     */
    private final ReviewSearch search;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    @Deprecated
    GenreSearch() {
        this.search = null;
    }

    /**
     * コンストラクタ.
     *
     * @param search レビュー検索
     */
    @Inject
    public GenreSearch(final ReviewSearch search) {

        this.search = search;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable view() {

        final List<Genre> genreList = new ArrayList<>();
        genreList.add(new Genre(new GenreId(1l), new GenreName("アクション")));
        genreList.add(new Genre(new GenreId(2l), new GenreName("RPG")));
        genreList.add(new Genre(new GenreId(3l), new GenreName("シミュレーション")));

        final ReviewListView reviewView = new ReviewListView(search.searchNewReviews(5));
        final GenreSearchView genreSearchView = new GenreSearchView(genreList);
        final GenreSearchResultView genreSearchResult = new GenreSearchResultView(genreSearchView, reviewView);

        return new Viewable("/genreSearch/genreSearchResult", genreSearchResult);
    }
}
