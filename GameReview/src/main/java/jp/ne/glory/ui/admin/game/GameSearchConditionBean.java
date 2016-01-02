package jp.ne.glory.ui.admin.game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.QueryParam;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.ui.genre.GenreBean;
import lombok.Getter;
import lombok.Setter;

/**
 * ゲーム検索条件Bean.
 *
 * @author Junki Yamada
 */
public class GameSearchConditionBean {

    /**
     * タイトル.
     */
    @Getter
    @Setter
    @QueryParam("title")
    private String title;

    /**
     * CEROレーティング.
     */
    @Getter
    @Setter
    @QueryParam("ceroRating")
    private CeroRating ceroRating;

    /**
     * ジャンルID.
     */
    @Getter
    @Setter
    @QueryParam("genreId")
    private Long genreId;

    /**
     * ページ番号.
     */
    @Getter
    @Setter
    @QueryParam("pageNumber")
    private Integer pageNumber;

    /**
     * ジャンルリスト.
     */
    @Getter
    private List<GenreBean> genres;

    /**
     * CEROレーティングリスト.
     */
    @Getter
    private final List<CeroRating> ratings;

    /**
     * コンストラクタ.
     */
    public GameSearchConditionBean() {

        ratings = Arrays.stream(CeroRating.values())
                .filter(v -> !CeroRating.Empty.equals(v))
                .collect(Collectors.toList());
    }

    /**
     * コンストラクタ.
     *
     * @param paramGenres ジャンルリスト
     */
    public GameSearchConditionBean(final List<Genre> paramGenres) {

        this();
        setGenres(paramGenres);
    }

    /**
     * ジャンルリストを設定する.<br>
     * Genreエンティティは内部で画面表示用のBeanに変換される.
     *
     * @param paramGenres ジャンルリスト
     */
    public final void setGenres(final List<Genre> paramGenres) {

        this.genres = paramGenres.stream()
                .map(GenreBean::new)
                .collect(Collectors.toList());
    }
}
