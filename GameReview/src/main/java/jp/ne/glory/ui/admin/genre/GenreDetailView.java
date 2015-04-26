package jp.ne.glory.ui.admin.genre;

import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreName;
import lombok.Getter;

/**
 * ジャンル詳細View.
 *
 * @author Junki Yamada
 */
public class GenreDetailView {

    /**
     * ラベル：名前.
     */
    @Getter
    private final String nameLabel = GenreName.LABEL;

    /**
     * ジャンルID.
     */
    @Getter
    private final long genreId;

    /**
     * ジャンル名.
     */
    @Getter
    private final String name;

    /**
     * コンストラクタ.
     *
     * @param genre ジャンル
     */
    public GenreDetailView(final Genre genre) {

        genreId = genre.getId().getValue();
        name = genre.getName().getValue();
    }
}
