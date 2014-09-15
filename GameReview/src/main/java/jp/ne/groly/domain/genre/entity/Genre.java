package jp.ne.groly.domain.genre.entity;

import jp.ne.groly.domain.genre.value.GenreId;
import jp.ne.groly.domain.genre.value.GenreName;

/**
 * ジャンル.
 * @author Junki Yamada
 */
public class Genre {

    /** ラベル */
    public static final String LABEL = "ジャンル";
 
    /** ジャンルID. */
    public final GenreId id;

    /** ジャンル名. */
    public final GenreName name;

    /**
     * コンストラクタ.
     * @param paramId ジャンルID
     * @param paramName ジャンル名
     */
    public Genre(final GenreId paramId, final GenreName paramName) {

        id = paramId;
        name = paramName;
    }

    /**
     * 登録済みのゲームかを判定する.
     * @return 登録済みの場合：true、未登録の場合：false
     */
    public boolean isRegistered() {
        
        return id.isSetValue;
    }    
}
