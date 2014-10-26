package jp.ne.glory.ui.genre;

import jp.ne.glory.domain.genre.entity.Genre;

/**
 * ジャンル情報.
 *
 * @author Junki Yamada
 */
public class GenreBean {

    /**
     * タイトル.
     */
    public final String title;

    /**
     * ID.
     */
    public final long id;

    /**
     * コンストラクタ.
     *
     * @param genre ジャンル
     */
    public GenreBean(final Genre genre) {

        title = genre.name.value;
        id = genre.id.value;
    }
}
