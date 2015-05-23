package jp.ne.glory.domain.game.value.search;

import java.util.List;
import jp.ne.glory.domain.common.value.SearchResults;
import jp.ne.glory.domain.game.entity.Game;

/**
 * ゲーム検索結果.
 *
 * @author Junki Yamada
 */
public class GameSearchResults extends SearchResults<GameSearchCondition, Game> {

    /**
     * コンストラクタ.
     *
     * @param condition 検索条件
     * @param genreList ジャンルリスト
     * @param allCount 全件件数
     */
    public GameSearchResults(final GameSearchCondition condition, final List<Game> genreList, final int allCount) {

        super(condition, genreList, allCount);
    }
}
