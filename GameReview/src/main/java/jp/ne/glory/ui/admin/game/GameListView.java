package jp.ne.glory.ui.admin.game;

import java.util.List;
import jp.ne.glory.web.common.PagerInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * ゲーム一覧View.
 *
 * @author Junki Yamada
 */
public class GameListView {

    /**
     * 検索条件.
     */
    @Getter
    @Setter
    private GameSearchConditionBean condition;

    /**
     * ゲーム一覧.
     */
    @Getter
    @Setter
    private List<GameBean> games;

    /**
     * *
     * ページャ情報.
     */
    @Getter
    @Setter
    private PagerInfo pager;
}
