package jp.ne.glory.ui.admin.genre;

import javax.ws.rs.FormParam;
import lombok.Getter;
import lombok.Setter;

/**
 * ジャンル検索Bean.
 *
 * @author Junki Yamada
 */
public class GenreSearchConditionBean {

    /**
     * ジャンル名.
     */
    @Getter
    @Setter
    @FormParam("genreName")
    private String genreName;
}
