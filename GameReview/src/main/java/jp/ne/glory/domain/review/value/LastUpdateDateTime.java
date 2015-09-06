package jp.ne.glory.domain.review.value;

import jp.ne.glory.common.type.DateTimeValue;

/**
 * 最終更新日時.
 *
 * @author Junki Yamada
 */
public class LastUpdateDateTime {

    /**
     * ラベル.
     */
    public static final String LABEL = "最終更新日時";

    /**
     * 日時情報.
     */
    private final DateTimeValue value;

    /**
     * コンストラクタ.
     *
     * @param paramValue 日時情報.
     */
    public LastUpdateDateTime(final DateTimeValue paramValue) {

        value = paramValue;
    }

    /**
     * 空の値を返却する.
     *
     * @return 空の値が設定されたオブジェクト
     */
    public static LastUpdateDateTime empty() {

        return new LastUpdateDateTime(DateTimeValue.empty());
    }

    /**
     * 値を返却する.<br>
     * 値がNullの場合はNullが返却される。
     *
     * @return 値
     */
    public DateTimeValue getValue() {
        return value;
    }
}
