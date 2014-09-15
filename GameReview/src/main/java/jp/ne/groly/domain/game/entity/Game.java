package jp.ne.groly.domain.game.entity;

import java.util.Optional;
import jp.ne.groly.domain.game.value.CeroRating;
import jp.ne.groly.domain.game.value.GameId;
import jp.ne.groly.domain.game.value.SiteUrl;
import jp.ne.groly.domain.game.value.Title;
import jp.ne.groly.domain.genre.value.GenreId;

/**
 * ゲーム.
 * @author Junki Yamada
 */
public class Game {

    /** ラベル. */
    public static final String LABEL = "ゲーム";

    /** ゲームID. */
    public final GameId id;

    /** タイトル. */
    public final Title title;

    /** 公式サイトのURL. */
    public SiteUrl url;

    /** CEROレーティング. */
    public CeroRating ceroRating;

    /** ジャンルID. */
    public GenreId genreId;

    /**
     * コンストラクタ.<br>
     * ゲームIDとタイトルを設定する。
     * 
     * @param paramId ゲームID
     * @param paramTitle タイトル
     */
    public Game(final GameId paramId, final Title paramTitle) {

        id = Optional.ofNullable(paramId).orElse(GameId.notNumberingValue());
        title = Optional.ofNullable(paramTitle).orElse(Title.empty());
        url = SiteUrl.empty();
        ceroRating = CeroRating.Empty;
        genreId = GenreId.notNumberingValue();
    }

    /**
     * 登録済みのゲームかを判定する.
     * @return 登録済みの場合：true、未登録の場合：false
     */
    public boolean isRegistered() {
        
        return id.isSetValue;
    }

}
