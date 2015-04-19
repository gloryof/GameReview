package jp.ne.glory.ui.admin.genre;

import java.util.List;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.ui.genre.GenreBean;
import lombok.Getter;
import lombok.Setter;

/**
 * ジャンル一覧View.
 *
 * @author Junki Yamada
 */
public class GenreListView {

    /**
     * ジャンル名ラベル.
     */
    @Getter
    private final String nameLabel = GenreName.LABEL;

    /**
     * ジャンル検索条件.
     */
    @Setter
    @Getter
    private GenreSearchConditionBean condition;

    /**
     * ジャンル一覧.
     */
    @Setter
    @Getter
    private List<GenreBean> genres;
}
