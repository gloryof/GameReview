package jp.ne.glory.infra.db.game.entity;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;

/**
 * ゲーム本体と付随情報を紐付け得た結果のEntity.
 *
 * @author Junki Yamad
 */
@Entity
public class GameInfo {

    /**
     * ゲームID.
     */
    @Getter
    @Setter
    private long gameId;

    /**
     * タイトル.
     */
    @Getter
    @Setter
    private String title;

    /**
     * ジャンルID.
     */
    @Getter
    @Setter
    private long genreId;

    /**
     * CERO-ID.
     */
    @Getter
    @Setter
    private long ceroId;

    /**
     * URL.
     */
    @Getter
    @Setter
    private String siteUrl;
}
