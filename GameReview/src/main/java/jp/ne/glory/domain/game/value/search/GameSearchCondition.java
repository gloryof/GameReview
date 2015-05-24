package jp.ne.glory.domain.game.value.search;

import jp.ne.glory.domain.common.value.SearchCondition;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.value.GenreId;
import lombok.Getter;
import lombok.Setter;

/**
 * ゲーム検索条件.
 *
 * @author Junki Yamada
 */
public class GameSearchCondition extends SearchCondition {

    /**
     * タイトル.
     */
    @Getter
    @Setter
    private Title title;

    /**
     * CEROレーティング.
     */
    @Getter
    @Setter
    private CeroRating ceroRating;

    /**
     * ジャンルID.
     */
    @Getter
    @Setter
    private GenreId genreId;

    /**
     * ゲームの並び順.<br>
     * デフォルトは {@link GameSearchOrder#IdDesc}。
     */
    @Getter
    @Setter
    private GameSearchOrder order;
}
