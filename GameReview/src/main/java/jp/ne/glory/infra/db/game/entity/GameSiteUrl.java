package jp.ne.glory.infra.db.game.entity;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/**
 * ゲームサイトURL.
 *
 * @author Junki Yamada
 */
@Entity
public class GameSiteUrl {

    /**
     * ゲームID.
     */
    @Getter
    @Setter
    @Id
    private long gameId;

    /**
     * URL.
     */
    @Getter
    @Setter
    private String siteUrl;
}
