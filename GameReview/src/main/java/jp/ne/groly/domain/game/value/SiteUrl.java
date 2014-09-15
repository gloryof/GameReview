package jp.ne.groly.domain.game.value;

import jp.ne.groly.domain.common.annotation.MaxLength;
import jp.ne.groly.domain.common.annotation.ValidCharacters;
import jp.ne.groly.domain.common.annotation.param.ValidCharcterType;
import jp.ne.groly.domain.common.type.InputText;

/**
 * 公式サイトのURL.
 * @author Junki Yamada
 */
@MaxLength(value = 2083, label = SiteUrl.LABEL)
@ValidCharacters(value = ValidCharcterType.OnlySingleByteChars, label = SiteUrl.LABEL)
public class SiteUrl extends InputText {

    /** ラベル */
    public static final String LABEL = "サイトURL";

    /**
     * コンストラクタ.
     * 
     * @param paramValue 値
     */
    public SiteUrl(final String paramValue) {

        super(paramValue);
    }

    /**
     * 空オブジェクトを返却する.
     * @return  空オブジェクト
     */
    public static SiteUrl empty() {

        return new SiteUrl("");
    }
}
