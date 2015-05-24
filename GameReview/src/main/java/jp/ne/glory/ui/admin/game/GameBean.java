package jp.ne.glory.ui.admin.game;

import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.CeroRating;
import lombok.Getter;

/**
 * ゲーム情報Bean.
 *
 * @author Junki Yamada
 */
public class GameBean {

    /**
     * ゲームID.
     */
    @Getter
    private final long gameId;

    /**
     * タイトル.
     */
    @Getter
    private final String title;

    /**
     * サイトURL.
     */
    @Getter
    private final String siteUrl;

    /**
     * CEROレーティング.
     */
    @Getter
    private final CeroRating ceroRating;

    /**
     * ジャンルID.
     */
    @Getter
    private final Long genreId;

    /**
     * コンストラクタ.
     *
     * @param game ゲームエンティティ
     */
    public GameBean(final Game game) {

        gameId = game.getId().getValue();
        title = game.getTitle().getValue();
        siteUrl = game.getUrl().getValue();
        ceroRating = game.getCeroRating();
        genreId = game.getGenreId().getValue();
    }
}
