package jp.ne.glory.infra.db.repository.entity;

import jp.ne.glory.domain.genre.value.search.GenreSearchCondition;
import lombok.Getter;

/**
 * ジャンル検索パラメータ.
 *
 * @author Junki Yamada
 */
public class GenreSearchParam {

    /**
     * ジャンル名.
     */
    @Getter
    private final String genreName;

    /**
     * コンストラクタ.
     *
     * @param condition ジャンル検索条件
     */
    public GenreSearchParam(final GenreSearchCondition condition) {

        this.genreName = condition.getName().getValue();
    }
}
