
package jp.ne.glory.domain.review.value;

import jp.ne.glory.common.type.DateTimeValue;

/**
 * レビュー投稿日時.
 *
 * @author Junki Yamada
 */
public class PostDateTime {

    /**
     * 日時情報.
     */
    private final DateTimeValue value;

    /**
     * コンストラクタ.
     *
     * @param paramValue 日時情報.
     */
    public PostDateTime(final DateTimeValue paramValue) {

        value = paramValue;
    }

    /**
     * 空の値を返却する.
     *
     * @return 空の値が設定されたオブジェクト
     */
    public static PostDateTime empty() {

        return new PostDateTime(DateTimeValue.empty());
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
