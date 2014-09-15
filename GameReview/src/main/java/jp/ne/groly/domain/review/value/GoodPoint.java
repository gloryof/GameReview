package jp.ne.groly.domain.review.value;

import jp.ne.groly.domain.common.annotation.Required;
import jp.ne.groly.domain.common.type.InputText;

/**
 * 良い点.
 * @author Junki Yamada
 */
@Required(label = GoodPoint.LABEL)
public class GoodPoint extends InputText {

    /** ラベル. */
    public static final String LABEL = "良い点";

    /**
     * コンストラクタ.<br>
     * 値を設定する
     * @param paramValue 値 
     */
    public GoodPoint(final String paramValue) {

        super(paramValue);
    }

    /**
     * 空の値オブジェクトを返却する.
     * @return 良い点オブジェクト
     */
    public static GoodPoint empty() {

        return new GoodPoint("");
    }
}
