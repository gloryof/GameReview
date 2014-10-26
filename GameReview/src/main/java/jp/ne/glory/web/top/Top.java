package jp.ne.glory.web.top;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.ui.genre.GenreSearchView;
import jp.ne.glory.ui.top.TopView;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * Top画面.
 *
 * @author Admin
 */
@Path("top")
public class Top {

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

        final TopView topView = new TopView(new GenreSearchView(genreList));

        return new Viewable("/top/top", topView);
    }
}
