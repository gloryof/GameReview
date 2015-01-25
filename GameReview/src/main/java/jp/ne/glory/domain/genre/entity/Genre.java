package jp.ne.glory.domain.genre.entity;

import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import lombok.Getter;

/**
 * ジャンル.
 * @author Junki Yamada
 */
public class Genre {

    /** ラベル */
    public static final String LABEL = "ジャンル";
 
    /**
     * ジャンルID.
     */
    @Getter
    private final GenreId id;

    /**
     * ジャンル名.
     */
    @Getter
    private final GenreName name;

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
        
        return id.isSetValue();
    }    
}
