package jp.ne.glory.ui.admin.genre;

import javax.ws.rs.FormParam;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreName;
import lombok.Getter;
import lombok.Setter;

/**
 * ジャンル編集View.
 *
 * @author Junki Yamada
 */
public class GenreEditView {

    /**
     * ラベル：ジャンル名.
     */
    @Getter
    private final String nameLabel = GenreName.LABEL;

    /**
     * ジャンルID.
     */
    @FormParam("genreId")
    @Getter
    @Setter
    private Long genreId;

    /**
     * ジャンル名.
     */
    @FormParam("name")
    @Getter
    @Setter
    private String name;

    /**
     * 入力チェック結果.
     */
    @Getter
    private final ValidateErrors errors = new ValidateErrors();

    /**
     * コンストラクタ.
     */
    public GenreEditView() {

        genreId = null;
        name = null;
    }

    /**
     * コンストラクタ.
     *
     * @param genre ジャンル
     */
    public GenreEditView(final Genre genre) {

        genreId = genre.getId().getValue();
        name = genre.getName().getValue();
    }
}
