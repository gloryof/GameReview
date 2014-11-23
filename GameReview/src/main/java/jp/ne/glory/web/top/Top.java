package jp.ne.glory.web.top;

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
import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.review.ReviewView;
import jp.ne.glory.ui.top.TopView;
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
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    @Deprecated
    Top() {
        this.search = null;
    }

    /**
     * コンストラクタ.
     *
     * @param injectSearch レビュー検索
     */
    @Inject
    public Top(final ReviewSearch injectSearch) {

        search = injectSearch;
    }

    /**
     * 画面を表示する.
     *
     * @return ビューオブジェクト
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable view() {

        final List<Genre> genreList = new ArrayList<>();
        genreList.add(new Genre(new GenreId(1l), new GenreName("アクション")));
        genreList.add(new Genre(new GenreId(2l), new GenreName("RPG")));
        genreList.add(new Genre(new GenreId(3l), new GenreName("シミュレーション")));

        final ReviewView reviewView = new ReviewView(search.searchNewReviews(5));
        final GenreSearchView genreSearchView = new GenreSearchView(genreList);
        final TopView topView = new TopView(genreSearchView, reviewView);

        return new Viewable("/top/top", topView);
    }
}
