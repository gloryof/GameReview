package jp.ne.glory.ui.genre;

import jp.ne.glory.domain.genre.entity.Genre;
import lombok.Getter;

/**
 * ジャンル情報.
 *
 * @author Junki Yamada
 */
public class GenreBean {

    /**
     * タイトル.
     */
    @Getter
    private final String title;

    /**
     * ID.
     */
    @Getter
    private final long id;

    /**
     * コンストラクタ.
     *
     * @param genre ジャンル
     */
    public GenreBean(final Genre genre) {

        title = genre.getName().getValue();
        id = genre.getId().getValue();
    }
}
