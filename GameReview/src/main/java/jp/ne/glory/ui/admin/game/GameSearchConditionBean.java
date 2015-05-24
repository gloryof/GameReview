package jp.ne.glory.ui.admin.game;

import jp.ne.glory.domain.game.value.CeroRating;
import lombok.Getter;
import lombok.Setter;

/**
 * ゲーム検索条件Bean.
 *
 * @author Junki Yamada
 */
public class GameSearchConditionBean {

    /**
     * タイトル.
     */
    @Getter
    @Setter
    private String title;

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
    private Long genreId;

}
