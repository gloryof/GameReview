package jp.ne.glory.infra.db.game.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/**
 * ゲーム基本情報エンティティ.
 *
 * @author Junki Yamada
 */
@Entity
public class GameBaseInfo {

    /**
     * ゲームID.
     */
    @Getter
    @Setter
    @Id
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
     * ロック管理タイムスタンプ.
     */
    @Getter
    @Setter
    private LocalDateTime lockUpdateTimestamp;
}
