package jp.ne.groly.domain.review.value;

import jp.ne.groly.domain.common.annotation.Required;
import jp.ne.groly.domain.common.type.InputText;

/**
 * 悪い点.
 * @author Junki Yamada
 */
@Required(label = BadPoint.LABEL)
public class BadPoint extends InputText {

    /** ラベル. */
    public static final String LABEL = "悪い点";

    /**
     * コンストラクタ.<br>
     * 値を設定する
     * @param paramValue 値 
     */
    public BadPoint(final String paramValue) {

        super(paramValue);
    }

    /**
     * 空の値オブジェクトを返却する.
     * @return 悪い点オブジェクト
     */
    public static BadPoint empty() {

        return new BadPoint("");
    }
}
