package jp.ne.glory.ui.admin.review;

import javax.ws.rs.QueryParam;
import jp.ne.glory.ui.admin.game.GameSearchConditionBean;
import lombok.Getter;
import lombok.Setter;

/**
 * レビュー検索Bean.
 *
 * @author Junki Yamada
 */
public class ReviewSearchConditionBean {

    /**
     * 検索期間.
     */
    @Getter
    @Setter
    private DateRange range = new DateRange();

    /**
     * ゲーム検索条件.
     */
    @Getter
    @Setter
    private GameSearchConditionBean gameCondition = new GameSearchConditionBean();

    /**
     * ページ番号.
     */
    @Getter
    @Setter
    @QueryParam("pageNumber")
    private Integer pageNumber = 1;

}
