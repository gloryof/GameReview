package jp.ne.glory.ui.admin.game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.ui.genre.GenreBean;
import lombok.Getter;
import lombok.Setter;

/**
 * ゲーム編集View.
 *
 * @author Junki Yamada
 */
public class GameEditView {

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
     * 入力チェック結果.
     */
    @Getter
    private final ValidateErrors errors = new ValidateErrors();

    /**
     * CEROレーティングリスト.
     */
    @Getter
    private final List<CeroRating> ratings;

    /**
     * ジャンルリスト.
     */
    @Getter
    @Setter
    private List<GenreBean> genres;

    /**
     * ゲームID.
     */
    @Getter
    @Setter
    @FormParam("gameId")
    private Long gameId;

    /**
     * タイトル.
     */
    @Getter
    @Setter
    @FormParam("title")
    private String title;

    /**
     * 公式サイトのURL.
     */
    @Getter
    @Setter
    @FormParam("url")
    private String url;

    /**
     * CEROレーティング.
     */
    @Getter
    @Setter
    @FormParam("ceroRating")
    private CeroRating ceroRating;

    /**
     * ジャンルID.
     */
    @Getter
    @Setter
    @FormParam("genreId")
    private Long genreId;

    /**
     * コンストラクタ.
     */
    public GameEditView() {

        ratings = Arrays.stream(CeroRating.values())
                .filter(v -> v.getId() != null)
                .collect(Collectors.toList());
    }

    /**
     * コンストラクタ.
     *
     * @param game ゲーム
     */
    public GameEditView(final Game game) {

        this();
        gameId = game.getId().getValue();
        title = game.getTitle().getValue();
        url = game.getUrl().getValue();
        ceroRating = game.getCeroRating();
        genreId = game.getGenreId().getValue();
    }
}
