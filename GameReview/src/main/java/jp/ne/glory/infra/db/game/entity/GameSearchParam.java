package jp.ne.glory.infra.db.game.entity;

import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.game.value.search.GameSearchCondition;
import jp.ne.glory.domain.game.value.search.GameSearchOrder;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.infra.db.common.entity.RecordLimits;
import lombok.Getter;

/**
 * ゲーム検索条件.
 *
 * @author Junki Yamada
 */
public class GameSearchParam {

    /**
     * 検索件数制限.
     */
    @Getter
    private final RecordLimits limits;

    /**
     * タイトル.
     */
    @Getter
    private String title = null;

    /**
     * CEROレーティング.
     */
    @Getter
    private Long ceroRating = null;

    /**
     * ジャンルID.
     */
    @Getter
    private Long genreId;

    /**
     * ゲームの並び順.
     */
    @Getter
    private final GameSearchOrder order;

    /**
     * コンストラクタ.
     *
     * @param condition 検索条件
     */
    public GameSearchParam(final GameSearchCondition condition) {

        this.limits = new RecordLimits(condition);
        this.order = condition.getOrder();

        final Title paramTitle = condition.getTitle();
        if (paramTitle != null && !paramTitle.getValue().isEmpty()) {

            title = paramTitle.getValue();
        }

        final CeroRating paramCero = condition.getCeroRating();
        if (paramCero != null && !CeroRating.Empty.equals(paramCero)) {

            ceroRating = paramCero.getId();
        }

        final GenreId paramGenreId = condition.getGenreId();
        if (paramGenreId != null && paramGenreId.isSetValue()) {

            genreId = paramGenreId.getValue();
        }
    }
}
