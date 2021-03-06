package jp.ne.glory.web.admin.genre;

import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.genre.value.search.GenreSearchCondition;
import jp.ne.glory.domain.genre.value.search.GenreSearchResults;
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
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    Genres() {
        this.genreSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param genreList ジャンル検索
     */
    @Inject
    public Genres(final GenreSearch genreList) {

        this.genreSearch = genreList;
    }

    /**
     * 初期表示.
     *
     * @return ジャンル一覧
     */
    @GET
    @Transactional(Transactional.TxType.REQUIRED)
    public Viewable view() {

        final List<GenreBean> genres = genreSearch.getAllGenres().stream()
                .map(GenreBean::new)
                .collect(Collectors.toList());

        final GenreListView view = new GenreListView();
        view.setCondition(new GenreSearchConditionBean());
        view.setGenres(genres);

        return new Viewable(PagePaths.GENRE_LIST, view);
    }

    /**
     * ジャンル検索.
     *
     * @param conditionBean 検索条件
     * @return ジャンル一覧
     */
    @POST
    @Path("search")
    @Transactional(Transactional.TxType.REQUIRED)
    public Viewable search(@BeanParam final GenreSearchConditionBean conditionBean) {

        final GenreSearchCondition condition = new GenreSearchCondition();
        condition.setName(new GenreName(conditionBean.getGenreName()));

        final GenreSearchResults result = genreSearch.search(condition);
        final List<GenreBean> genres = result.getResults().stream()
                .map(GenreBean::new)
                .collect(Collectors.toList());

        final GenreListView view = new GenreListView();
        view.setCondition(conditionBean);
        view.setGenres(genres);

        return new Viewable(PagePaths.GENRE_LIST, view);
    }
}
