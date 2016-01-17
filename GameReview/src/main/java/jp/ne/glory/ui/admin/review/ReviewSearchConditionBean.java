package jp.ne.glory.ui.admin.review;

import java.time.LocalDate;
import java.util.ArrayList;
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
 * レビュー検索Bean.
 *
 * @author Junki Yamada
 */
public class ReviewSearchConditionBean {

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
     * レビュー日時From.
     */
    @Getter
    @Setter
    @QueryParam("from")
    private LocalDate from;

    /**
     * レビュー日時To.
     */
    @Getter
    @Setter
    @QueryParam("to")
    private LocalDate to;
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
    private final List<GenreBean> genres;

    /**
     * CEROレーティングリスト.
     */
    @Getter
    private final List<CeroRating> ratings;

    /**
     * コンストラクタ.
     */
    public ReviewSearchConditionBean() {

        this.genres = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    /**
     * コンストラクタ.
     *
     * @param paramGenres ジャンルリスト
     */
    public ReviewSearchConditionBean(final List<Genre> paramGenres) {

        this.ratings = Arrays.stream(CeroRating.values())
                .filter(v -> !CeroRating.Empty.equals(v))
                .collect(Collectors.toList());

        this.genres = paramGenres.stream()
                .map(GenreBean::new)
                .collect(Collectors.toList());
    }
}
