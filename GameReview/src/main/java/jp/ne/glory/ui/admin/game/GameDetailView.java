package jp.ne.glory.ui.admin.game;

import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import lombok.Getter;
import lombok.Setter;

/**
 * ゲーム詳細View.
 *
 * @author Junki Yamada
 */
public class GameDetailView {

    /**
     * ラベル：タイトル.
     */
    @Getter
    private final String tileLabel = Title.LABEL;

    /**
     * ラベル：URL.
     */
    @Getter
    private final String urlLabel = SiteUrl.LABEL;

    /**
     * ラベル：CEROレーティング.
     */
    @Getter
    private final String ceroLabel = CeroRating.LABEL;

    /**
     * ラベル：ジャンル.
     */
    @Getter
    private final String genreLabel = Genre.LABEL;

    /**
     * ゲームID.
     */
    @Getter
    @Setter
    private Long gameId;

    /**
     * タイトル.
     */
    @Getter
    @Setter
    private String title;

    /**
     * URL.
     */
    @Getter
    @Setter
    private String url;

    /**
     * CEROレーティング.
     */
    @Getter
    @Setter
    private CeroRating ceroRating;

    /**
     * ジャンル名.
     */
    @Getter
    @Setter
    private String genreName;

    /**
     * コンストラクタ.
     *
     * @param game ゲーム
     */
    public GameDetailView(final Game game) {

        this(game, null);
    }

    /**
     * コンストラクタ.
     *
     * @param game ゲーム
     * @param genre ジャンル
     */
    public GameDetailView(final Game game, final Genre genre) {

        this.gameId = game.getId().getValue();
        this.title = game.getTitle().getValue();
        this.url = game.getUrl().getValue();
        this.ceroRating = game.getCeroRating();

        if (genre != null) {
            this.genreName = genre.getName().getValue();
        }
    }
}
