package jp.ne.glory.domain.genre.value.search;

import jp.ne.glory.domain.common.value.SearchCondition;
import jp.ne.glory.domain.genre.value.GenreName;
import lombok.Getter;
import lombok.Setter;

/**
 * ジャンル検索条件.
 *
 * @author Junki Yamada
 */
public class GenreSearchCondition extends SearchCondition {

    /**
     * ジャンル名.
     */
    @Getter
    @Setter
    private GenreName name;
}
