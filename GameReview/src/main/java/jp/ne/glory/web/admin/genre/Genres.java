package jp.ne.glory.web.admin.genre;

import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jp.ne.glory.application.genre.GenreList;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.genre.GenreListView;
import jp.ne.glory.ui.admin.genre.GenreSearchConditionBean;
import jp.ne.glory.ui.genre.GenreBean;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * ジャンル一覧.
 *
 * @author Junki Yamada
 */
@CertifyTarget
@RequestScoped
@Path("/admin/genres")
public class Genres {

    /**
     * ジャンルリスト.
     */
    private final GenreList genreList;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    Genres() {
        this.genreList = null;
    }

    /**
     * コンストラクタ.
     *
     * @param genreList ジャンル検索
     */
    @Inject
    public Genres(final GenreList genreList) {

        this.genreList = genreList;
    }

    /**
     * 初期表示.
     *
     * @return ジャンル一覧
     */
    @GET
    public Viewable view() {

        final List<GenreBean> genres = genreList.getAllGenres().stream()
                .map(GenreBean::new)
                .collect(Collectors.toList());

        final GenreListView view = new GenreListView();
        view.setCondition(new GenreSearchConditionBean());
        view.setGenres(genres);

        return new Viewable(PagePaths.GENRE_LIST, view);
    }
}
